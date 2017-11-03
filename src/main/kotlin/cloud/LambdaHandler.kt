package cloud

import cloud.dqn.utilities.LongId
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.io.*

/**
 * For additional AWS specific parameters:
 *   http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
 * Getting started:
 *   http://docs.aws.amazon.com/lambda/latest/dg/get-started-step4-optional.html
 * Deployment package help:
 *   http://docs.aws.amazon.com/lambda/latest/dg/with-s3-example-deployment-pkg.html
 * CloudWatch logging requirement
 *   http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html
 */

class LambdaHandler: RequestStreamHandler {

    companion object {
        val DEFAULT_CHARSET = Charsets.UTF_8 // TODO require api gateway to have this character encoding
    }

    fun handler(long: Long, context: Context): String {
        val logger = context.logger
        val longId = LongId(long)
        val str = "LongId($long) = ${longId.str}"
        logger.log(str)
        return longId.str
    }

    /**
     * Utilizing AWS Lambda Proxy integrations (No aws integrated response after lambda finishes)
     *  Required to write out to output stream a json in string format with the following keys:
     *      body: as a String
     *      statusCode: Int
     *      headers: Json Object
     */
    override fun handleRequest(input: InputStream, output: OutputStream, context: Context) {
        val jsonParser = JsonParser()
        val response = JsonObject()
        val input = try {
            val inputStreamReader = InputStreamReader(input, DEFAULT_CHARSET)
            val json = jsonParser.parse(inputStreamReader) // throws : JsonParseException
            if (!json.isJsonObject) {
                context.logger.log(json.toString())
                response.addProperty("statusCode", 400)
                response.addProperty("body", """{"error":"$jsonParser is not formatted as a json"}""")
            } else {
                response.addProperty("statusCode", 200)
                val params = JsonObject()
                params.add("params", json)
                response.addProperty("body", params.toString())
            }
        } catch (e: JsonParseException) {
            response.addProperty("statusCode", 400)
            response.addProperty("body", """{"error":$e}""")
        } catch (e: Exception) {
            response.addProperty("statusCode", 400)
            response.addProperty("body", """{"error":$e}""")
        }
        val headers = JsonObject()
        headers.addProperty("Content-Type", "application/json")
        response.add("headers", headers)

        response.addProperty("isBase64Encoded", false)
        val responseStr = response.toString()

        context.logger.log(responseStr)

        output.write(responseStr.toByteArray(DEFAULT_CHARSET))
    }


}
package cloud.dqn.utilities.project.models.singletons

/**
 * Base companion object to be used for Singletons
 * Lifted from: https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
 *
 * Example usage:
    class Manager private constructor(context: Context) {
        init {
            // Init using context argument
        }

        companion object : SingletonHolder<Manager, Context>(::Manager)
    }

    Manager.getInstance(context).doStuff()
 */
open class SingletonHolder<out T, in ARGS>(constructorMethod: (ARGS) -> T) {
    private var constructorMethod: ((ARGS) -> T)? = constructorMethod
    @Volatile private var instance: T? = null

    fun getInstance(arg: ARGS): T {

        instance?.let { return it }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = constructorMethod!!(arg)
                instance = created
                constructorMethod = null
                created
            }
        }
    }
}
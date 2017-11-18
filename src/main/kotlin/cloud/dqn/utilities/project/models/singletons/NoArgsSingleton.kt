package cloud.dqn.utilities.project.models.singletons

/**
 * Example of Multi Attribute, singleton with NO arguments
 *
 * to get instance
 *
 * NoArgsSingleton.instance
 */
class NoArgsSingleton private constructor() {
    val attr0: Any?
    val attr1: Any?

    init {
        attr0 = null
        attr1 = null
    }

    companion object {
        val instance: NoArgsSingleton by lazy { Holder.INSTANCE }
    }

    private object Holder {
        val INSTANCE = NoArgsSingleton()
    }
}
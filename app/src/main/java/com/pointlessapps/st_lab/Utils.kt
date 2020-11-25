package com.pointlessapps.st_lab

object Utils {
    open class SingletonHolder<T : Any, in A>(creator: (A?) -> T) {
        private var creator: ((A?) -> T)? = creator

        @Volatile
        internal var instance: T? = null

        fun init(arg: A? = null): T {
            if (instance != null) return instance!!

            return synchronized(this) {
                if (instance != null) instance!!
                else {
                    val created = creator!!(arg)
                    instance = created
                    creator = null
                    created
                }
            }
        }
    }
}

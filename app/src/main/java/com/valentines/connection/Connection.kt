package com.valentines.connection

class Connection {
    private val BASE_URL: String = ""

    // create companion object to statically access functions
    companion object {

        /*----------------------------------
            LOGIN FUNCTION

            Desc: logs in a user
            Params:
                Username -> String
                Password -> String
            Returns:
                Bool
        ------------------------------------*/
        fun login(username: String, password: String): Boolean {
            /*
                temp implementation

                return true if username == john and password == pw
             */
            return username == "john" && password == "pw"
        }
    }
}
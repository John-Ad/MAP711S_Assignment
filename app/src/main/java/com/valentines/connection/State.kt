package com.valentines.connection

class State {
    private var userType: Int = 0
    private var username: String = ""

    private constructor() {
        userType = 0
        username = ""
    }

    companion object {
        private var state: State? = null
        fun getInstance(): State {
            if (this.state == null) {
                this.state = State()
            }

            return state!!
        }

        val USER_ADMIN = 1
        val USER_EMPLOYEEE = 2
    }

    fun setUserType(userType: Int) {
        this.userType = userType
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun getUserType(): Int {
        return this.userType
    }

    fun getUsername(): String {
        return this.username
    }

}
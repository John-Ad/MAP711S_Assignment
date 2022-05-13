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
    }

    public fun setUserType(userType: Int) {
        this.userType = userType
    }

    public fun setUsername(username: String) {
        this.username = username
    }
}
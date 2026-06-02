package com.creativehazio.data.user.mapper

import com.creativehazio.data.user.data.remote.UserDto
import com.creativehazio.data.user.domain.User

fun UserDto.toUser(userDto: UserDto) : User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
    )
}
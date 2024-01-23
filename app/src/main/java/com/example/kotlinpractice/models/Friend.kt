package com.example.kotlinpractice.models

import java.io.Serializable

data class Friend(
    val id: Int,
    val name: String,
    val userId: String?,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDayOfMonth: Int,
    val remarks: String?,
    val pictureUrl: String?,
    val age: Int?
) : Serializable {
    constructor(
        name: String,
        userId: String?,
        birthYear: Int,
        birthMonth: Int,
        birthDayOfMonth: Int,
        remarks: String?,
        pictureUrl: String?,
        age: Int?
    ) : this(-1, name, userId, birthYear, birthMonth, birthDayOfMonth, remarks, pictureUrl, age)

    override fun toString(): String {

        if(remarks.isNullOrEmpty() || pictureUrl.isNullOrEmpty())
        {
            return "Name:$name\nBirthday:$birthDayOfMonth/$birthMonth/$birthYear\nage:$age"
        }
        else if(remarks.isNullOrEmpty())
        {
            return "Name:$name\nBirthday:$birthDayOfMonth/$birthMonth/$birthYear\nage:$age \n $pictureUrl"
        }
        else if(pictureUrl.isNullOrEmpty())
        {
            return "Name:$name\nBirthday:$birthDayOfMonth/$birthMonth/$birthYear\nage:$age \n $remarks"
        }
        else
        {
            return "Name:$name\nBirthday:$birthDayOfMonth/$birthMonth/$birthYear\nage:$age\nComments:$remarks\n$pictureUrl"
        }
    }
}

package com.task.ecommercebluefunder.state_management

sealed class Resources<T>(val data:T?=null , val exception :Exception ?=null ) {

    class Loading<T>(data :T?=null):Resources<T>(data)
    class Sucess<T>( data:T): Resources<T>(data)
    class Error<T>(message:java.lang.Exception , data:T?=null):Resources<T>(data , message)

    override fun toString(): String {
        return when(this){
            is Loading ->{
                "Loading [data$data]"
            }
            is Sucess ->{
                "Sucess [data$data]"
            }
            is Error ->{
                "Error [Message $exception ,  data$data]"
            }
        }
    }
}
package com.limelight.module

/**
 * Created by zhb5145 on 2020/12/25
 */
const val KEY_TYPE = "type"
const val KEY_PROGRAM = "program"
const val KEY_ARGS = "args"
const val KEY_CODEC = "codec"
const val KEY_SIGN = "sign"

class CmdData(var type: Boolean = false, var program: String, var args: List<String> = listOf(),
              var codec: String = "gbk", var sign: String) {


    fun toJsonString(): String {
        var listString = "[";
        args.forEachIndexed { index, it ->
            if (index != args.size - 1)
                listString += "\"$it\","
            else
                listString += "\"$it\""
        }
        listString += "]"
        return "{\"$KEY_TYPE\":\"${if (type) "attach" else "detached"}\"" +
                ",\"$KEY_PROGRAM\":\"$program\",\"$KEY_ARGS\":$listString," +
                "\"$KEY_SIGN\":\"$sign\",\"$KEY_CODEC\":\"$codec\"}"

    }
}
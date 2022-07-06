package com.github.win15.getgitcommitmsg.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

/**
 *@version : 1.0
 *@auther : 齐马的作品
 *@date : 2022/7/7 1:01
 *@description:
 */
class HelloAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val commitMsg = getGitCommitMsgByDate()
        Messages.showMessageDialog(e.getProject(), commitMsg, "您的git提交记录", Messages.getInformationIcon());
    }

    private fun getGitCommitMsgByDate(): String? {
        // 获取当天日期
        var now = LocalDateTime.now()
        now = now.minusDays(2)
        val min = now.with(LocalTime.MIN)
        val max = now.with(LocalTime.MAX)
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val minFormat = dateTimeFormatter.format(min)
        val maxFormat = dateTimeFormatter.format(max)
        val userCommand = "git config --get user.name"
        val user = execCMD(userCommand)
        val msgCommand =
            "git log --after=\"$minFormat\" --before=\"$maxFormat\" --author=\"$user\" --pretty=format:%s --no-merges --reverse"
        val msg = execCMD(msgCommand)
        val split = msg.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val collect = Arrays.stream(split)
            .collect(Collectors.toList())
        println("msg=$msg")
        println("collect = $collect")
        return msg
    }

    //执行cmd命令，获取返回结果
    fun execCMD(command: String?): String {
        val sb = StringBuilder()
        try {
            val process = Runtime.getRuntime().exec(command)
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
        } catch (e: Exception) {
            return e.toString()
        }
        return sb.toString()
    }
}
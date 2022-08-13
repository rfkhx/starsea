package top.ntutn.starsea.updateloop

import com.google.auto.service.AutoService
import top.ntutn.starsea.arch.ApplicationContext
import top.ntutn.starsea.util.ConfigUtil
import top.ntutn.starsea.util.LoggerOwner
import top.ntutn.starsea.util.slf4jLoggerOwner
import top.ntutn.starseasdk.v2.BotContentProvider
import top.ntutn.starseasdk.v2.ITextChatContext

/**
 * 向管理员提供一个“关闭”命令
 * FIXME 关机时关闭命令未得到确认导致无法开机
 */
@AutoService(BotContentProvider::class)
class RebootCommandSupport: BotContentProvider, LoggerOwner by slf4jLoggerOwner<RebootCommandSupport>() {
    companion object {
        private const val CMD = "power"
    }

    override val pluginName: String = CMD

    override val registeredCommands: List<String> = listOf(CMD)

    override fun onTextMessage(context: ITextChatContext): Boolean {
        if (context.command == CMD && context.params?.firstOrNull() == ConfigUtil.configBean.superPwd) {
            logger.info("closing by user command.")
            ApplicationContext.shutdown()
            context.replyWithText("service is closing.")
            return true
        }
        return super.onTextMessage(context)
    }
}
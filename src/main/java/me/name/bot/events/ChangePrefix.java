package me.name.bot.events;

import me.name.bot.Misc.SqlUtil;
import me.name.bot.Misc.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangePrefix {

    private String currentDateTime;
    private String prefix;
    private String guildId;
    private String permittedRankId;
    private ResultSet rs;

    public void executeChangePrefixEvent(MessageReceivedEvent e, String[] messageArray) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SqlUtil sqlUtil = new SqlUtil();
        currentDateTime = simpleDateFormat.format(new Date());

        this.rs = sqlUtil.executeSelectSqlCommand("SELECT rankId FROM t_permission WHERE guildID = '" + guildId + "' AND permissionID = 1;");

        this.permittedRankId = "";
        this.guildId = e.getGuild().getId();
        this.prefix = messageArray[2];
        try {
            this.rs.next();
            this.permittedRankId = this.rs.getString("rankId");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Util util = new Util();
        if (util.checkUserHasRole(e, this.permittedRankId)) {

            if (prefix.length() > 3) {
                e.getChannel().sendMessage("Prefix can only be up to 3 characters!").queue();
            } else {
                //CREATE TABLE
                //CHANGE SQL
                sqlUtil.executeSqlCommand("" + guildId + prefix + currentDateTime);
                e.getChannel().sendMessage("Prefix changed to: '" + this.prefix + "' !").queue();

            }

        }

    }
}

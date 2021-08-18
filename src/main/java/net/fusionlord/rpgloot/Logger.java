package net.fusionlord.rpgloot;

import org.apache.logging.log4j.Level;
import net.minecraftforge.fml.common.FMLLog;

public class Logger
{
    public static final String HEADER = "[RPGLoot] ";
    public static Logger INSTANCE;

    public static void slog(Level level, String s)
    {
        INSTANCE.log(level, s);
    }

    public Logger()
    {
        INSTANCE = this;
    }

    public void log(Level logLevel, Object msg)
    {
        FMLLog.log.log(logLevel, HEADER + msg);
    }

    public void warn(Object msg)
    {
        log(Level.WARN, msg);
    }

    public void info(Object msg)
    {
        log(Level.INFO, msg);
    }

    public void error(Object msg)
    {
        log(Level.ERROR, msg);
    }
}
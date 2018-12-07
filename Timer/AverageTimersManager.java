package Timer;

import java.util.LinkedHashMap;
import java.util.Map;

public class AverageTimersManager
{
    protected LinkedHashMap<String, AverageTimer> timers;
    protected int                                 printPadSize = 35;

    public AverageTimersManager ()
    {
        timers = new LinkedHashMap<> ();
    }

    public synchronized long startBlock (String _timerName)
    {
        if (!timers.containsKey (_timerName))
        {
            timers.put (_timerName, new AverageTimer (_timerName));
        }

        return timers.get (_timerName).start ();
    }

    public synchronized double stopBlock (String _timerName, long _timeStartIdentifier)
    {
        if (!timers.containsKey (_timerName))
        {
            return -1;
        }

        return timers.get (_timerName).stop (_timeStartIdentifier);
    }

    public synchronized double getAverageTimeSeconds (String _timerName)
    {
        if (!timers.containsKey (_timerName))
        {
            return -1;
        }

        return timers.get (_timerName).getAverageTimeSeconds ();
    }

    public synchronized LinkedHashMap<String, Double> getAverageTimeSecondsToAllTimers ()
    {
        LinkedHashMap<String, Double> totalMap = new LinkedHashMap<> ();
        for (Map.Entry<String, AverageTimer> entry : timers.entrySet ())
        {
            totalMap.put (entry.getKey (), entry.getValue ().getAverageTimeSeconds ());
        }

        return totalMap;
    }

    @Override
    public String toString ()
    {
        String                        resultStr = "";
        LinkedHashMap<String, Double> totalMap  = getAverageTimeSecondsToAllTimers ();
        for (Map.Entry<String, Double> entry : totalMap.entrySet ())
        {
            resultStr += String.format ("%1$-" + printPadSize + "s", entry.getKey () + ":") + entry.getValue () + "\n";
        }

        return resultStr;
    }
}

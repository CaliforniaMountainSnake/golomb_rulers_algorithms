package Timer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Класс для замера СРЕДНЕГО времени выполнения для различных участков кода.
 */
public class AverageTimer
{
    protected HashMap<Long, Long> measuresMap;
    protected String              timerName;

    public AverageTimer (String _timerName)
    {
        timerName = _timerName;
        measuresMap = new HashMap<> ();
    }

    /**
     * Начать измерять время выполнения участка кода.
     *
     * @return Идентификатор для остановки таймера для данного участка (Время начала измерения участка кода).
     */
    public synchronized long start ()
    {
        long timeStart = System.currentTimeMillis ();
        measuresMap.put (timeStart, Long.valueOf (0));

        return timeStart;
    }

    public synchronized double stop (long _timeStartIdentifier)
    {
        if (!measuresMap.containsKey (_timeStartIdentifier))
        {
            return -1;
        }

        long timeEnd = System.currentTimeMillis ();
        measuresMap.put (_timeStartIdentifier, timeEnd);

        return ((double) timeEnd - _timeStartIdentifier) / 1000;
    }

    public synchronized double getAverageTimeSeconds ()
    {
        double sum = 0;
        for (Map.Entry<Long, Long> entry : measuresMap.entrySet ())
        {
            sum += (entry.getValue () - entry.getKey ());
        }

        return (sum / measuresMap.size () / 1000);
    }


}

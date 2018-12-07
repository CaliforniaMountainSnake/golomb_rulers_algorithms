package Golomb;

import Golomb.Generators.Exceptions.GeneratorException;
import Golomb.Generators.GolombRulerGenerator;
import Golomb.Rulers.Ruler;
import Timer.AverageTimersManager;

public class TestGolombSearchingAlgorithms
{

    protected static final String TIMER_NAME_TOTAL_TIME = "Total time";
    protected              int    rulerOrder;
    protected              int    rulerMaxLength;
    protected              int    rulerEnumerationMaxMarkValue;
    protected              int    maxIterationsPerAlgorithm;

    public TestGolombSearchingAlgorithms ()
    {
    }

    public void testAlgorithms (int _rulerOrder, int _rulerMaxLength, int _rulerEnumerationMaxMarkValue, GolombRulerGenerator[] _generators, int _maxIterationsPerAlgorithm)
    {
        // Инициализируем переменные.
        rulerOrder = _rulerOrder;
        rulerMaxLength = _rulerMaxLength;
        rulerEnumerationMaxMarkValue = _rulerEnumerationMaxMarkValue;
        maxIterationsPerAlgorithm = _maxIterationsPerAlgorithm;

        // Проверяем, чтобы параметры не противоречили друг другу.
        if (rulerEnumerationMaxMarkValue < rulerOrder - 1)
        {
            throw new RuntimeException ("Максимальное допустимое значение отметки не может быть меньше чем (прядок_линейки-1).");
        }
        if (rulerEnumerationMaxMarkValue < rulerMaxLength)
        {
            throw new RuntimeException ("Максимальное допустимое значение отметки не может быть меньше чем максимально разрешенная длина линейки");
        }

        // Запускаем тестирование.
        AverageTimersManager timersManager  = new AverageTimersManager ();
        long                 timeStartTotal = timersManager.startBlock (TIMER_NAME_TOTAL_TIME);
        for (GolombRulerGenerator generator : _generators)
        {
            System.out.println ("\nAlgorithm " + generator.getAlgorithmName () + ":");
            for (int i = 0; i < maxIterationsPerAlgorithm; i++)
            {
                String AlgorithmTimerName    = "Algorithm " + generator.getAlgorithmName ();
                long   AlgorithmTimerStartId = timersManager.startBlock (AlgorithmTimerName);
                System.out.print ((i + 1) + ") " + generator.getAlgorithmName () + ": ");

                try
                {
                    Ruler   ruler      = generator.generateGolombRuler (rulerOrder, rulerMaxLength, rulerEnumerationMaxMarkValue).convertToCanonicalForm ();
                    boolean isItGolomb = ruler.isItGolomb ();

                    System.out.print ("Generated: " + ruler + "; isGolomb: " + isItGolomb);
                }
                catch (GeneratorException e)
                {
                    System.out.print ("Can't generate, error: \"" + e.getLocalizedMessage () + "\"");
                }
                finally
                {
                    double generationTime = timersManager.stopBlock (AlgorithmTimerName, AlgorithmTimerStartId);
                    System.out.println ("; Time: " + generationTime);
                }
            }
        }

        timersManager.stopBlock (TIMER_NAME_TOTAL_TIME, timeStartTotal);
        System.out.println ();
        System.out.println ("Ruler order: " + _rulerOrder);
        System.out.println ("Ruler max length: " + _rulerMaxLength);
        System.out.println ("Ruler enumeration max mark value: " + _rulerEnumerationMaxMarkValue);
        System.out.println ("Time stats:\n" + timersManager);
    }
}

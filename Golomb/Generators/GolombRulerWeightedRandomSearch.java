package Golomb.Generators;

import Golomb.Generators.Exceptions.GeneratorException;
import Golomb.Rulers.Ruler;

import java.util.Random;

public class GolombRulerWeightedRandomSearch implements GolombRulerGenerator
{
    protected Random random;
    protected int    maxIterationsToRulerGeneration;

    /**
     * Создать объект поиска.
     *
     * @param _maxIterationsToRulerGeneration Максимальное число попыток для генерации линейки Голомба более высокого порядка из текущей неполной линейки Голомба. Иногда бывает так, что из текущей неполной линейки больше невозможно сгенерировать голомбовскую линейку с никакими числами в заданном пределе (от 0 до _enumerationMaxMarkValue).
     */
    public GolombRulerWeightedRandomSearch (int _maxIterationsToRulerGeneration)
    {
        maxIterationsToRulerGeneration = _maxIterationsToRulerGeneration;
        random = new Random ();
    }

    @Override
    public String getAlgorithmName ()
    {
        return "WeightedRandomSearch";
    }

    @Override
    public Ruler generateGolombRuler (int _order, int _maxLength, int _enumerationMaxMarkValue) throws GeneratorException
    {
        Ruler recordRuler = null;

        enumeration:
        while (true)
        {
            // На каждом шаге генерируем линейку Голомба.
            // Каждая неполностью собранная линейка - линейка Голомба.
            Ruler currentRuler;
            rulerGeneration:
            while (true)
            {
                currentRuler = new Ruler ();
                currentRuler.add (0);

                // Пытаемся сгенерировать отметку за отведенное число попыток.
                markGeneration:
                for (int markGenAttempts = 0; markGenAttempts < maxIterationsToRulerGeneration; markGenAttempts++)
                {
                    // Если собрали линейку нужной длины - выходим из цикла генерации линеек.
                    if (currentRuler.size () == _order)
                    {
                        break rulerGeneration;
                    }

                    // Если в текущую линейку возможно добавить это рандомное значение (если такого там еще нет)
                    // и если после этого линейка не стала Голомбовской, удаляем это значение.
                    int newValue = random.nextInt (_enumerationMaxMarkValue);
                    if (currentRuler.add (newValue) && !currentRuler.isItGolomb ())
                    {
                        currentRuler.remove (newValue);
                    }
                }
            }

            // Обновляем рекорд, если нужно.
            if (recordRuler == null || currentRuler.getLength () < recordRuler.getLength ())
            {
                recordRuler = currentRuler;
            }

            // Если длина рекорда меньше или равна требуемой - подбор окончен.
            if (recordRuler.getLength () <= _maxLength)
            {
                //System.out.println ("Golomb ruler found: " + recordRuler);
                break enumeration;
            }
        }

        if (recordRuler == null)
        {
            throw new GeneratorException ("Не удалось найти линейку с заданными ограничениями.");
        }
        return recordRuler;
    }
}

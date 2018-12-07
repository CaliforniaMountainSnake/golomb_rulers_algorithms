package Golomb.Generators;

import Golomb.Generators.Exceptions.GeneratorException;
import Golomb.Rulers.Ruler;

import java.util.Random;

public class GolombRulerRandomSearchGenerator implements GolombRulerGenerator
{
    protected Random random;

    public GolombRulerRandomSearchGenerator ()
    {
        random = new Random ();
    }

    @Override
    public String getAlgorithmName ()
    {
        return "RandomSearch";
    }

    @Override
    public Ruler generateGolombRuler (int _order, int _maxLength, int _enumerationMaxMarkValue) throws GeneratorException
    {
        Ruler recordRuler = null;

        enumeration:
        while (true)
        {
            Ruler currentRuler = new Ruler ();
            currentRuler.add (0);

            // Генерируем каждый элемент линейки полностью случайно.
            for (int i = 1; i < _order; i++)
            {
                while (true)
                {
                    if (currentRuler.add (random.nextInt (_enumerationMaxMarkValue)))
                    {
                        break;
                    }
                }

            }

            if (currentRuler.isItGolomb ())
            {
                if (recordRuler == null)
                {
                    recordRuler = currentRuler;
                }

                if (currentRuler.getLength () < recordRuler.getLength ())
                {
                    recordRuler = currentRuler;
                }
            }

            // Если длина рекорда меньше или равна требуемой - подбор окончен.
            if (recordRuler != null && recordRuler.getLength () <= _maxLength)
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

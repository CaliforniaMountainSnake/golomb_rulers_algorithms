package Golomb.Generators;

import Golomb.Generators.Exceptions.GeneratorException;
import Golomb.Rulers.Ruler;

import java.util.LinkedList;

/**
 * Полный перебор значений всех меток от 0 и до _enumerationMaxMarkValue.
 * Единственная эвристика - комбинации чисел в линейке всегда уникальны и не повторяются.
 * <p>
 * Расстояния до элементов не проверяются и никак не учитываются при подборе.
 * <p>
 * Генерятся сразу не линейки Голомба, а все возможные варианты линеек,
 * и уже из них выбираются линейки Голомба и сравниваются между собой.
 */
public class GolombRulerFullBruteForceGenerator implements GolombRulerGenerator
{
    public GolombRulerFullBruteForceGenerator ()
    {
    }

    public String getAlgorithmName ()
    {
        return "FullBruteForce";
    }

    public Ruler generateGolombRuler (int _order, int _maxLength, int _enumerationMaxMarkValue) throws GeneratorException
    {
        Ruler               recordRuler = null;
        LinkedList<Integer> seed        = new LinkedList (buildSeedRuler (_order));

        int lastIndex = seed.size () - 1;
        enumeration:
        while (true)
        {
            // На этом этапе у нас есть сформированная линейка. Можно проверить ее и прервать цикл, если требуется.
            //System.out.println ("Ruler: " + seed);

            // Все линейки начинаются с нуля - сдвиг элементов не требуется.
            // Длина линейки не отличается для обычной и зеркальной формы,
            // поэтому каждый раз конвертацию в минимальную форму проводить не требуется:
            // - она будет произведена на уровень выше для финальной сгенированной линейки.
            //Ruler currentRuler = Ruler.fromList (seed).convertToCanonicalForm ();
            //Ruler currentRuler = Ruler.fromList (seed).minimizeForm ();
            Ruler currentRuler = Ruler.fromList (seed);
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

            // Увеличиваем значение последнего элемента.
            if (seed.get (lastIndex) < _enumerationMaxMarkValue)
            {
                seed.set (lastIndex, (seed.get (lastIndex) + 1));
                continue enumeration;
            }

            // Пытаемся сдвигать индекс влево и засеивать с этой позиции.
            // Если в результате засеивания последний элемент превышает максимально допустимый,
            // - пытаемся засеивать с более левой позиции.
            // Если индекс левее сдвинуть невозможно - перебор окончен.
            int currentSeedingIndex = lastIndex;
            while (true)
            {
                //--currentSeedingIndex;
                // Используем продвинутый метод передвижения индекса.
                currentSeedingIndex -= (1 + getIndexDelta (seed, _enumerationMaxMarkValue));

                if (currentSeedingIndex < 1)
                {
                    // Индексы кончились, перебор окончен.
                    break enumeration;
                }

                // Пытаемся засеять.
                seed.set (currentSeedingIndex, seed.get (currentSeedingIndex) + 1);
                seedRulerFromPosition (seed, currentSeedingIndex);
                if (seed.get (lastIndex) > _enumerationMaxMarkValue)
                {
                    // Не удалось засеять - последний элемент превышает максимально допустимый.
                    // Пытаемся засеять с предыдущего индекса.
                    System.out.println ("Плохо засеяли");
                    continue;
                }

                break;
            }
        }

        if (recordRuler == null)
        {
            throw new GeneratorException ("Не удалось найти линейку с заданными ограничениями.");
        }
        return recordRuler;
    }

    /**
     * Создать начальную линейку вида [0, 1, 2, 3].
     *
     * @param _order
     *
     * @return
     */
    public Ruler buildSeedRuler (int _order)
    {
        Ruler ruler = new Ruler ();
        for (int i = 0; i < _order; i++)
        {
            ruler.add (i);
        }

        return ruler;
    }

    /**
     * Засеять линейку с данного индекса и до конца.
     * Заполнить линейку [0, 3, 1, 2] для индекса 1 элементами вида [0, 3, 4, 5].
     *
     * @param _ruler
     * @param _positionIndex
     */
    public void seedRulerFromPosition (LinkedList<Integer> _ruler, int _positionIndex)
    {
        int positionValue = _ruler.get (_positionIndex);
        for (int i = _positionIndex; i < _ruler.size (); i++)
        {
            _ruler.set (i, positionValue++);
        }
    }

    /**
     * Получить количество отличающихся на 1 элементов с конца линейки.
     *
     * @param _ruler
     * @param _enumerationMaxMarkValue
     *
     * @return
     */
    public int getIndexDelta (LinkedList<Integer> _ruler, int _enumerationMaxMarkValue)
    {
        int delta = 0;
        for (int i = _ruler.size () - 1; i > 0; i--)
        {
            if (_ruler.get (i) - _ruler.get (i - 1) == 1)
            {
                ++delta;
            }
            else
            {
                return delta;
            }
        }

        return delta;
    }
}

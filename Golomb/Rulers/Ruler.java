package Golomb.Rulers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ruler extends TreeSet<Integer>
{
    protected static int printPadSize = 20;

    /**
     * Создать линейку из массива.
     *
     * @param _arr Массив с отметками.
     *
     * @return
     */
    public static Ruler fromArray (int[] _arr)
    {
        Ruler ruler = new Ruler ();
        Collections.addAll (ruler, IntStream.of (_arr).boxed ().toArray (Integer[]::new));

        return ruler;
    }

    public static Ruler fromList (List<Integer> _list)
    {
        Ruler ruler = new Ruler ();
        ruler.addAll (_list);
        return ruler;
    }

    public Ruler copy ()
    {
        Ruler ruler = new Ruler ();
        ruler.addAll (this);
        return ruler;
    }

    public void printStats ()
    {
        Ruler canonicalForm = convertToCanonicalForm ();
        System.out.println (String.format ("%1$-" + printPadSize + "s", "L:" + getLength () + ", Ruler:") + this);
        System.out.println (String.format ("%1$-" + printPadSize + "s", "L:" + canonicalForm.getLength ()
                + ", Canonical:") + canonicalForm);
        System.out.println (String.format ("%1$-" + printPadSize + "s", "Is it Golomb:") + isItGolomb ());
        System.out.println (String.format ("%1$-" + printPadSize + "s", "Distances List:") + getDistancesList ());
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Является ли эта линейка линейкой Голомба?
     *
     * @return
     */
    public boolean isItGolomb ()
    {
        Integer[]          rulerArr  = this.stream ().toArray (Integer[]::new);
        ArrayList<Integer> distances = new ArrayList<> ();
        for (int i = 0; i < rulerArr.length; i++)
        {
            // Проверяем на неотрицательность.
            if (rulerArr[i] < 0)
            {
                return false;
            }

            for (int k = i + 1; k < rulerArr.length; k++)
            {
                // Проверяем дистанцию.
                int distance = Math.abs (rulerArr[i] - rulerArr[k]);
                if (distances.contains (distance))
                {
                    return false;
                }

                distances.add (distance);
            }
        }

        return true;
    }

    /**
     * Минимизировать форму линейки.
     * Если линейку можно зеркально отразить для минимизации второго элемента, вернуть зеркальную форму.
     *
     * @return
     */
    public Ruler minimizeForm ()
    {
        if (this.isEmpty ())
        {
            return this;
        }

        // Получим subset без первого и последнего элемента.
        // И проверим, нужно ли отражать линейку.
        SortedSet<Integer> internalSet = this.subSet (this.first (), false, this.last (), false);
        if (internalSet.isEmpty () || (this.last () - internalSet.last () > internalSet.first () - this.first ()))
        {
            return this;
        }

        // Зеркально отразим линейку.
        Ruler reversedSet = new Ruler ();
        for (int num : this)
        {
            reversedSet.add (Math.abs (num - this.last ()));
        }
        return reversedSet;
    }

    /**
     * Преобразовать линейку в канонический вид:
     * 1. Чтобы она начиналась с 0
     * 2. Была отсортирована в порядке возрастания.
     * 3. Была зеркально отражена для минимизации второго элемента.
     *
     * @return
     */
    public Ruler convertToCanonicalForm ()
    {
        Ruler sortedSet = new Ruler ();
        if (this.isEmpty ())
        {
            return this;
        }

        // Сделаем так, чтобы линейка начиналась с нуля.
        sortedSet.addAll (this.stream ().map ((x) -> x - Collections.min (this)).collect (Collectors.toSet ()));

        // Зеркально отразим, если требуется.
        return sortedSet.minimizeForm ();
    }

    /**
     * Получить длину линейки (значение максимального элемента)
     *
     * @return
     */
    public int getLength ()
    {
        if (this.isEmpty ())
        {
            return 0;
        }

        return this.last ();
    }

    /**
     * Получить лист расстояний между всеми элементами линейки.
     *
     * @return
     */
    public ArrayList getDistancesList ()
    {
        Integer[]          rulerArr  = this.stream ().toArray (Integer[]::new);
        ArrayList<Integer> distances = new ArrayList<> ();
        for (int i = 0; i < rulerArr.length; i++)
        {
            for (int k = i + 1; k < rulerArr.length; k++)
            {
                int distance = Math.abs (rulerArr[i] - rulerArr[k]);
                distances.add (distance);
            }
        }

        return distances;
    }

    /**
     * Являются ли все расстояния от этого значения до всех отметок линейки уникальными?
     *
     * @param _value Любое число.
     *
     * @return
     */
    public boolean isUniqueDistancesFromValue (int _value)
    {
        ArrayList<Integer> distancesFromValue = new ArrayList<> ();
        for (int mark : this)
        {
            int distance = Math.abs (mark - _value);
            if (distancesFromValue.contains (distance))
            {
                // Если это расстояние дублируется до уже существующих элементов, значение не подходит.
                return false;
            }

            distancesFromValue.add (distance);
        }

        return true;
    }
}

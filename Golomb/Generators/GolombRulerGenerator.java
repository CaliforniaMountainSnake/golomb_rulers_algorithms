package Golomb.Generators;

import Golomb.Generators.Exceptions.GeneratorException;
import Golomb.Rulers.Ruler;

public interface GolombRulerGenerator
{
    /**
     * Получить линейку Голомба заданного порядка и заданной максимальной длины.
     *
     * @param _order                   Порядок получаемой линейки.
     * @param _maxLength               Ее максимальная длина.
     * @param _enumerationMaxMarkValue Максимальное разрешенное значение отметки при переборе.
     *
     * @return
     */
    Ruler generateGolombRuler (int _order, int _maxLength, int _enumerationMaxMarkValue) throws GeneratorException;

    /**
     * Получить название алгоритма поиска линейки Голомба.
     *
     * @return
     */
    String getAlgorithmName ();
}

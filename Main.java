import Golomb.Generators.GolombRulerFullBruteForceGenerator;
import Golomb.Generators.GolombRulerGenerator;
import Golomb.Generators.GolombRulerRandomSearchGenerator;
import Golomb.Generators.GolombRulerWeightedRandomSearch;
import Golomb.TestGolombSearchingAlgorithms;

public class Main
{
    public static void main (String[] args)
    {
        // Тестируем генераторы.
        testGenerators ();
    }

    public static void testGenerators ()
    {
        int rulerOrder                   = 7;
        int rulerMaxLength               = 25;
        int rulerEnumerationMaxMarkValue = 30;
        //----------------
        int maxIterationsPerAlgorithm = 100;

        GolombRulerGenerator[] generators = {
                new GolombRulerFullBruteForceGenerator (),
                new GolombRulerRandomSearchGenerator (),
                new GolombRulerWeightedRandomSearch (100),
        };

        TestGolombSearchingAlgorithms testGolombSearchingAlgorithms = new TestGolombSearchingAlgorithms ();
        testGolombSearchingAlgorithms.testAlgorithms (rulerOrder, rulerMaxLength, rulerEnumerationMaxMarkValue, generators, maxIterationsPerAlgorithm);
    }
}

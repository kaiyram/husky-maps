package seamfinding;

import graphs.Edge;
import seamfinding.energy.EnergyFunction;

import java.util.*;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        List<Integer> result = new ArrayList<>();
        double[][] pathCosts = new double[picture.width()][picture.height()];
        for (int y = 0; y < picture.height(); y++) {
            pathCosts[0][y] = f.apply(picture, 0, y);
        }
        fillCostTable(picture, f, pathCosts);
        int minIndex = 0;
        double minVal = pathCosts[picture.width() - 1][minIndex];
        for (int y = 1; y < picture.height(); y++) {
            if (pathCosts[picture.width() - 1][y] < minVal) {
                minVal = pathCosts[picture.width() - 1][y];
                minIndex = y;
            }
        }
        result.add(minIndex);
        findMinSeam(picture, pathCosts, minIndex, result);
        Collections.reverse(result);
        return result;
    }

    private void findMinSeam(Picture picture, double[][] pathCosts, int index, List<Integer> result) {
        int currRow = index;
        for(int x = picture.width() - 2; x >= 0; x--) {
            int bestRow = currRow;
            double bestCost = Double.MAX_VALUE;

            for (int y = -1; y <  2; y++) {
                int candidateY = currRow + y;
                if (candidateY < picture.height() && candidateY >= 0) {
                    double val = pathCosts[x][candidateY];
                    if (val < bestCost) {
                        bestCost = val;
                        bestRow = candidateY;
                    }
                }
            }
            result.add(bestRow);
            currRow = bestRow;
        }
    }

    private void fillCostTable(Picture picture, EnergyFunction f, double[][] pathCosts) {
        for(int x = 1; x < picture.width(); x++) {
            for(int y = 0; y < picture.height(); y++) {
                double minPrev = Double.MAX_VALUE;
                for (int i = -1; i < 2; i++) {
                    if (y + i >= 0 && y + i < picture.height()) {
                        double candidate = pathCosts[x - 1][y + i];
                        if (candidate < minPrev) {
                            minPrev = candidate;
                        }
                    }
                }
                pathCosts[x][y] = minPrev + f.apply(picture, x, y);
            }
        }
    }

}

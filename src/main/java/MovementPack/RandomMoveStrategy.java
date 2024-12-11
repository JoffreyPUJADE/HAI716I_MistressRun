/*package MovementPack;

import AlgoPack.Pair;
import CharacterPack.Student;
import TilePack.Candy;
import TilePack.Tile;

public class RandomMoveStrategy implements MoveStrategy {
    @Override
    public void (Character character, Pair<Tile, int[]> currentPosition, Pair<Tile, int[]> targetPosition) {
        
        Candy nearestCandy = student.findNearestCandy();

        if (nearestCandy == null) {
            return;
        }

        boolean movedToCandy = student.goToNearestCandy();

        if (movedToCandy && student.candyIsAtOneCaseOrLess()) {
           
            student.incrementCandyCounter();
            System.out.println("Candy picked up!");
        }
        student.goToChair();
    }

    @Override
    public void move(Student student, Pair<Tile, int[]> currentPosition, Pair<Tile, int[]> targetPosition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
*/
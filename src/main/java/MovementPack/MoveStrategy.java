package MovementPack;

import AlgoPack.Pair;
import CharacterPack.Student;
import TilePack.Tile;

public interface MoveStrategy {

    void move(Student student, Pair<Tile, int[]> currentPosition, Pair<Tile, int[]> targetPosition);
}

package MovementPack;

import java.util.ArrayList;
import java.util.List;

import AlgoPack.Node;
import AlgoPack.Pair;
import CharacterPack.Character;
import TilePack.Chair;
import TilePack.Tile;

public abstract class Strategy extends Character {

    public Strategy(String spriteSheet, Chair chair, int i, int j) {
        super(spriteSheet, chair, i, j);
    }


    public abstract List<Node> findPath(Pair<Tile, int[]> start, Pair<Tile, int[]> goal, ArrayList<ArrayList<Tile>> tiles, ArrayList<ArrayList<Character>> characters);

    
    @Override
    public abstract boolean isTouched();

  
    @Override
    public abstract boolean isEscaping();
}

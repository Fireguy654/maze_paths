package backend.academy.maze.model.dsf;

public class Tree {
    private int size = 1;
    private Tree parent = null;

    public void merge(Tree tree) {
        if (tree == null) {
            return;
        }
        if (parent != null || tree.parent != null) {
            getTop().merge(tree.getTop());
        } else if (size < tree.size) {
            tree.merge(this);
        } else {
            tree.parent = this;
            size += tree.size;
        }
    }

    public boolean inSameTree(Tree tree) {
        if (tree == null) {
            return false;
        }
        return getTop() == tree.getTop();
    }

    private Tree getTop() {
        if (parent == null) {
            return this;
        }
        Tree top = parent.getTop();
        parent = top;
        return top;
    }
}

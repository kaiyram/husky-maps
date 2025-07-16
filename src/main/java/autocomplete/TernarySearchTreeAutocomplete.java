package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            overallRoot = addAll(term, overallRoot, 0);
        }
    }

    private Node addAll(CharSequence term, Node node, int index) {
        char currChar = term.charAt(index);
        if (node == null) {
            node = new Node(currChar);
        }
        if (currChar < node.data) {
            node.left = addAll(term, node.left, index);
        } else if (currChar > node.data) {
            node.right = addAll(term, node.right, index);
        } else {
            if (index == term.length() - 1) {
                node.isTerm = true;
            } else {
                node.mid = addAll(term, node.mid, index + 1);
            }
        }
        return node;
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }
        if (overallRoot == null) {
            return result;
        }
        Node start = get(prefix.toString(), overallRoot, 0);
        if (start == null) {
            return result;
        }
        if (start.isTerm) {
            result.add(prefix.toString());
        }
        collect(new StringBuilder(prefix), start.mid, result);
        return result;
    }

    private Node get(String prefix, Node node, int index) {
        if (node == null) {
            return null;
        }
        char currChar = prefix.charAt(index);
        if (currChar < node.data) {
            return get(prefix, node.left, index);
        } else if (currChar > node.data) {
            return get(prefix, node.right, index);
        } else if (index < prefix.length() - 1) {
            return get(prefix, node.mid, index + 1);
        } else {
            return node;
        }
    }

    private void collect(StringBuilder prefix, Node node, List<CharSequence> result) {
        if (node == null) {
            return;
        }
        collect(prefix, node.left, result);

        if (node.isTerm) {
            result.add(prefix.toString() + node.data);
        }
        collect(prefix.append(node.data), node.mid, result);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(prefix, node.right, result);
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}

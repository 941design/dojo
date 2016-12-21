import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/* Learnings: At first, I did not collect Strings in Sets but Node
 * objects.  However, a hashCode() method was missing, which
 * (fortunately) was critical.  Tests timed out.  Collecting names
 * (String) instead of objects fixed the issue.  Not a problem in
 * Haskell, though. ;-)
 */
public class SweepingTrees {

    protected static class Node {

        final String s;
        final String name;
        final String parent;
        final boolean valid;

        protected Node(String s) {
            this.s = s;
            final String[] split = s.split(",");
            this.name = split[0];
            this.parent = split[1];
            this.valid = split[2].equals("valid");
        }

        protected boolean isValid() {
            return valid;
        }

        protected boolean isRoot() {
            return parent.isEmpty();
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public Set<String> determineValidIds(List<String> dirtyTree) {

        final List<Node> nodes = parseNodes(dirtyTree);
        final Set<String> valid = new HashSet<>();
        final Set<String> invalid = new HashSet<>();

        while (!nodes.isEmpty()) {
            final Iterator<Node> iter = nodes.iterator();
            while (iter.hasNext()) {
                final Node node = iter.next();
                if (!node.isValid() || invalid.contains(node.parent)) {
                    invalid.add(node.name);
                    iter.remove();
                }
                else if (node.isRoot() || valid.contains(node.parent)) {
                    valid.add(node.name);
                    iter.remove();
                }
            }
        }
        return valid;
    }

    private List<Node> parseNodes(List<String> dirtyTree) {
        return dirtyTree.stream()
                .map(Node::new)
                .collect(Collectors.toList());
    }

}

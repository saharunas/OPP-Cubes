package ethanjones.cubes.core.util.iterator;

import java.util.ArrayList;
import java.util.List;

public final class IteratorClient {

    private IteratorClient() {
    }

    public static List<String> inspectAll(IterableCollection<Inspectable> collection, int maxItems) {
        List<String> out = new ArrayList<String>();
        if (collection == null) {
            out.add("<null collection>");
            return out;
        }

        CubesIterator<Inspectable> it = collection.createIterator();
        int count = 0;
        while (it.hasMore()) {
            if (maxItems > 0 && count >= maxItems) {
                out.add("... (truncated, maxItems=" + maxItems + ")");
                break;
            }
            Inspectable element = it.getNext();
            out.add(element == null ? "<null element>" : element.inspect());
            count++;
        }

        if (count == 0) out.add("<empty>");
        return out;
    }
}
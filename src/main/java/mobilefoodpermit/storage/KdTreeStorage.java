package mobilefoodpermit.storage;

import mobilefoodpermit.models.MobileFoodPermit;
import org.locationtech.jts.index.kdtree.KdNode;

import java.util.List;

public interface KdTreeStorage<S> {
     List<KdNode> getAllNodes();
     List<S> dataFromNodes(List<KdNode> nodes);
     S insert(S permit);
}

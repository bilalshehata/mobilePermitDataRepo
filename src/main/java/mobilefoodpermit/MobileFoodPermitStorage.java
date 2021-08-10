package mobilefoodpermit;

import mobilefoodpermit.models.MobileFoodPermit;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.kdtree.KdNode;
import org.locationtech.jts.index.kdtree.KdTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MobileFoodPermitStorage {
    //Tree storage system is optimised towards range queries  for spatial data.
    private static KdTree treeStorage;

    public MobileFoodPermitStorage() {
        treeStorage = new KdTree();
    }

    public static MobileFoodPermit insert(MobileFoodPermit permit) {
        KdNode node = getNodeFromPermit(permit);
        if (node != null) {

            List<MobileFoodPermit> nodeData = (List<MobileFoodPermit>) node.getData();
            nodeData.add(permit);
        }
        if (node == null) {

            List newList = new ArrayList<>();
            newList.add(permit);
            MobileFoodPermitStorage.treeStorage.insert(new Coordinate(permit.getX(), permit.getY()), newList);
        }
        return permit;
    }

    private static KdNode getNodeFromPermit(MobileFoodPermit permit) {
        return MobileFoodPermitStorage.treeStorage.query(new Coordinate(permit.getX(), permit.getY()));
    }

    private static List<KdNode> getAllNodes() {
        Envelope env = new Envelope(new Coordinate(0, 0));
        env.expandBy(99999999);
        List<KdNode> allNodes = MobileFoodPermitStorage.treeStorage.query(env);
        return allNodes;
    }

    public static List<MobileFoodPermit> getAllPermits() {
        return permitsFromNodes(getAllNodes());
    }

    public static MobileFoodPermit getByLocationId(String locationId) {
        return getAllPermits().stream()
                .filter(x -> x.getLocationId().equals(locationId))
                .collect(Collectors.toList()).get(0);
    }


    public static List<MobileFoodPermit> getByRadius(Coordinate center, double radius) {
        Envelope env = new Envelope(new Coordinate(center));
        env.expandBy(radius);
        List<KdNode> nodesInRadius = MobileFoodPermitStorage.treeStorage.query(env);
        return permitsFromNodes(nodesInRadius);

    }

    private static List<MobileFoodPermit> permitsFromNodes(List<KdNode> nodes){
        ArrayList<MobileFoodPermit> AllPermits = new ArrayList<>();
        nodes.forEach(x -> {
            if ((List<MobileFoodPermit>) x.getData() != null) {
                AllPermits.addAll((List<MobileFoodPermit>) x.getData());
            }
        });

        return AllPermits;
    }


}

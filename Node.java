import java.util.ArrayList;

public class Node {
    ArrayList<Card> nodeLst= new ArrayList<>();
    Node leftChild;//the left child of the node
    Node rightChild;//the right child of the node
    Node parent;//parent of the node
    private int nodeAttack;//attack value of the node
    private int height;//height of the node
    private int hMax;//it will help me to reduce the operations...
    // ...if the priority of the card surviving can't happen in the entire tree,itt will automatically pass to the 3d and 4th priority
    int aMax;// same logic applies here.

    public Node(Card card) {
        this(card, null);//if the node is root
    }

    public Node(Card card, Node parent) {//if not root
        this.leftChild =null;
        this.rightChild = null;
        nodeLst.add(card);
        this.parent=parent;
        this.height=1;
        this.nodeAttack=card.getAcur();
    }

    public int getHeight() {//getter of height
        return height;
    }

    public int getNodeAttack() {//getter of nodeattack
        return nodeAttack;
    }

    public int getHMax() {//getter of max health in the whole subtrees+node itself
        return hMax;
    }

    public void setHMax(int hMax) {//setter of hmax
        this.hMax = hMax;
    }
    public int getAMax() {//getter of max attack in the whole subtrees+node itself
        return aMax;
    }

    public void setAMax(int aMax) {//setter of max attack
        this.aMax = aMax;
    }

    public void hMaxvalue(){//calculates the max health
        if (nodeLst == null || nodeLst.isEmpty()) {
            int leftH = (leftChild != null) ? leftChild.getHMax() : 0;
            int rightH = (rightChild != null) ? rightChild.getHMax() : 0;
            setHMax(Math.max(leftH, rightH));
            return;
        }
        int selfH;
        if (nodeLst!=null){
            selfH = nodeLst.get(nodeLst.size() - 1).getHcur();}
        else{
            selfH=0;}
        int leftH = (leftChild != null) ? leftChild.getHMax() : 0;
        int rightH = (rightChild != null) ? rightChild.getHMax() : 0;

        setHMax(Math.max(selfH, Math.max(leftH, rightH)));
    }

    public void aMaxvalue() {//calculates the max attack

        int selfA = nodeAttack;
        int leftA = (leftChild != null) ? leftChild.getAMax() : 0;
        int rightA = (rightChild != null) ? rightChild.getAMax() : 0;

        if (nodeLst == null || nodeLst.isEmpty()) {
            setAMax(Math.max(leftA, rightA));
        } else {
            setAMax(Math.max(selfA, Math.max(leftA, rightA)));
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }//sets the height

    public void setNodeAttack(int nodeAttack) {
        this.nodeAttack = nodeAttack;
    }//sets the attack of the node

}

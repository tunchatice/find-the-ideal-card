import java.util.ArrayList;

import static java.lang.Math.max;

public class Tree {
    Node root;//root of the tree
    int size;
    private int count;//the number of card in the tree
    Score score=new Score();//keeps the score

    public int getCount() {//getter of count
        return count;}

    public void setCount(int count) {//setter of count
        this.count = count;}

    public Tree(){//default consturctor
        this.count=0;
        this.root=null;
        this.size=0;}
    public int hMissing(Card c){
        return c.getHbase()-c.getRevivalProgress();
    }




    public Node nodeInsertingDeck(Node node, Card card){// it insert or reinserts the card to the deck
        if (card.getHcur() <= 0) {
            throw new IllegalStateException("[BUG] 0 HP card inserted into deck: " + card.getId());
        }
        if (node==null){
            Node n = new Node(card, null); // no parent for now
            card.belongingNode=n;//assigns the card which node it belongs
            count+=1;//increments the number of cards in the deck
            n.hMaxvalue();//sets the node hmax again
            n.aMaxvalue();//sets the node amax again
            return n;
        }
        else {
            if((node.getNodeAttack()<card.getAcur())){//if the current node's attack is smaller
                node.rightChild=nodeInsertingDeck(node.rightChild,card);//it will recursively look for
                //the right node
                if (node.rightChild != null) {node.rightChild.parent = node;}}//it will asign the parent node

            else if((node.getNodeAttack()>card.getAcur())){//if the current node's attack is greater
                node.leftChild=nodeInsertingDeck(node.leftChild,card);//it will recursively look for
                //the left node
                if (node.leftChild != null) {node.leftChild.parent = node;}}//it will assign the parent node
            else {// if same attack


                for (int i = 0; i < node.nodeLst.size(); i++) {
                        if (card.getHcur() < node.nodeLst.get(i).getHcur()) {//if card's health is smaller it will insert
                            //// to the previous index of the bigger card
                            card.belongingNode=node;//assign the belonging node
                            node.nodeLst.add(i, card);
                            count+=1;//increment
                            break;}
                        else if (card.getHcur() == node.nodeLst.get(i).getHcur()) {//if same value it will go until a different hvalue,
                            // then increment as the last of same hvalues
                            card.belongingNode=node;
                            int index=i;
                            while(index < node.nodeLst.size() &&
                                    node.nodeLst.get(index).getHcur() == card.getHcur())
                                index++;
                            node.nodeLst.add(index, card);
                            count+=1;//increment
                            break;}
                        else if (i == node.nodeLst.size() - 1) {
                            card.belongingNode=node;
                            node.nodeLst.add(card);
                            count+=1;
                            break;}
                }

            }
        }
        //rebalance the tree
        //update the values
        //rotate if neccessary
        node.setHeight(1+Math.max(getHeight(node.leftChild),getHeight(node.rightChild)));
        node.hMaxvalue();
        node.aMaxvalue();
        int caseN= checkBalance(node);
        switch (caseN) {
            case 1:
                node = rightRotate(node);
                break;
            case 2:
                node.leftChild = leftRotate(node.leftChild);
                node = rightRotate(node);
                break;
            case 3:
                node.rightChild = rightRotate(node.rightChild);
                node = leftRotate(node);
                break;
            case 4:
                node = leftRotate(node);
                break;
            default:
                break;
        }
        //reassign the parents

        if (node.leftChild != null) {node.leftChild.parent = node;}
        if (node.rightChild != null) {node.rightChild.parent = node;}
        return (node);}


    public Node nodeInsertingDiscardPile(Node node, Card card){

        if (node==null){
            Node n = new Node(card, null);
            card.belongingNode=n;
            return n;
        }
        else {
            int hlst=hMissing(node.nodeLst.get(0));
            int hcard=hMissing(card);
            if(hcard<hlst){
                node.leftChild=nodeInsertingDiscardPile(node.leftChild,card);
            if (node.leftChild != null) {node.leftChild.parent = node;} }

            else if(hcard>hlst){
                node.rightChild=nodeInsertingDiscardPile(node.rightChild,card);
                if (node.rightChild != null){ node.rightChild.parent = node;}}


                else {
                card.belongingNode=node;
                node.nodeLst.add(card);
                return node;
            }
            }
        node.setHeight(1+Math.max(getHeight(node.leftChild),getHeight(node.rightChild)));
        node.hMaxvalue();
        node.aMaxvalue();
        int caseN= checkBalance(node);
        switch (caseN) {
            case 1:
                node = rightRotate(node);
                break;
            case 2:
                node.leftChild = leftRotate(node.leftChild);
                node = rightRotate(node);
                break;
            case 3:
                node.rightChild = rightRotate(node.rightChild);
                node = leftRotate(node);
                break;
            case 4:
                node = leftRotate(node);
                break;
            default:
                break;
        }

        if (node.leftChild != null) node.leftChild.parent = node;
        if (node.rightChild != null) node.rightChild.parent = node;
        return (node);}




    //checking if the tree is blanced
    public int checkBalance(Node n){
        int balance = getHeight(n.leftChild) - getHeight(n.rightChild);
        // left side
        if (balance > 1) {
            if (n.leftChild != null) { // if left is not null
                if (getHeight(n.leftChild.leftChild) - getHeight(n.leftChild.rightChild) > 0) {
                    return 1; // LL case → right rotate
                } else {
                    return 2;} // LR case → left then right
            }
        }
        // right side
        else if (balance < -1) {
            if (n.rightChild != null) { // only if right is not null
                if (getHeight(n.rightChild.rightChild) - getHeight(n.rightChild.leftChild) > 0) {
                    return 4; // RR case → left rotate
                } else {
                    return 3;} // RL case → right then left
            }
        }
        // if balanced
        else {
            return 0;}
        return 0;
    }

    //applying the left rotation
    Node leftRotate(Node A) {
        if (A == null || A.rightChild == null) {return A;}// nothing to rotate
        Node B = A.rightChild;
        Node T2 = B.leftChild;
        B.leftChild = A;
        A.rightChild = T2;

        if (T2 != null) T2.parent = A;
        B.parent = A.parent;
        A.parent = B;

        if (B.parent != null) {
            if (B.parent.leftChild == A) B.parent.leftChild = B;
            else if (B.parent.rightChild == A) B.parent.rightChild = B;
        }

        // Update heights bottom-up
        A.setHeight(1 + Math.max(getHeight(A.leftChild), getHeight(A.rightChild)));
        B.setHeight(1 + Math.max(getHeight(B.leftChild), getHeight(B.rightChild)));
        A.hMaxvalue();
        A.aMaxvalue();
        B.hMaxvalue();
        B.aMaxvalue();
        propagateUpwards(B.parent);
        for (Card c : A.nodeLst) c.belongingNode = A;
        for (Card c : B.nodeLst) c.belongingNode = B;
        return B;
    }

    //applying the left rotation
    Node rightRotate(Node A) {
        if (A == null || A.leftChild == null) {return A;} // nothing to rotate

        Node B = A.leftChild;
        Node T2 = B.rightChild;

        B.rightChild = A;
        A.leftChild = T2;

        if (T2 != null) T2.parent = A;
        B.parent = A.parent;
        A.parent = B;

        if (B.parent != null) {
            if (B.parent.leftChild == A) B.parent.leftChild = B;
            else if (B.parent.rightChild == A) B.parent.rightChild = B;
        }
        // Update heights bottom-up
        A.setHeight(1 + Math.max(getHeight(A.leftChild), getHeight(A.rightChild)));
        B.setHeight(1 + Math.max(getHeight(B.leftChild), getHeight(B.rightChild)));
        A.hMaxvalue();
        A.aMaxvalue();
        B.hMaxvalue();
        B.aMaxvalue();
        propagateUpwards(B.parent);
        for (Card c : A.nodeLst) c.belongingNode = A;
        for (Card c : B.nodeLst) c.belongingNode = B;



        return B;
    }


    //finding the ideal card
    public Card findOptimalCard(Node root, int opAttack, int opHealth){
        //nothing to find
        Card defaultcard=null;
        if(root==null){
            return null;}

        if (root.nodeLst == null || root.nodeLst.isEmpty()) {
            return null;}


        //doesn't neccessarily mean it is going to be p1, just it might be p1
        else if (root.getHMax() > opAttack && root.getAMax() >= opHealth) { // priority 1 zone
            Card bestP1 = null;
            Card bestP2 = null;

            // current node scanning
            for (Card c : root.nodeLst) {
                if (c.getHcur() > opAttack && c.getAcur() >= opHealth) { // P1
                    if (bestP1 == null ||
                            (c.getAcur() == bestP1.getAcur() && c.getHcur() < bestP1.getHcur()))
                        bestP1 = c;
                }
                else if (c.getHcur() > opAttack && c.getAcur() < opHealth) { // P2
                    if (bestP2 == null || c.getAcur() > bestP2.getAcur())
                        bestP2 = c;
                }
            }

            Card leftBest = null, rightBest = null;

            if (root.leftChild != null && root.leftChild.getHMax() > opAttack)
                leftBest = findOptimalCard(root.leftChild, opAttack, opHealth);

            // left P1 first
            if (leftBest != null && leftBest.getHcur() > opAttack && leftBest.getAcur() >= opHealth)
                return leftBest;

            // current node P1
            if (bestP1 != null)
                return bestP1;

            //⃣checking right subtree
            if (root.rightChild != null && root.rightChild.getHMax() > opAttack)
                rightBest = findOptimalCard(root.rightChild, opAttack, opHealth);

            if (rightBest != null && rightBest.getHcur() > opAttack && rightBest.getAcur() >= opHealth)
                return rightBest;

            // Fallback to P2
            if (rightBest != null && rightBest.getHcur() > opAttack && rightBest.getAcur() < opHealth)
                return rightBest;

            if (bestP2 != null)
                return bestP2;


            if (leftBest != null && leftBest.getHcur() > opAttack && leftBest.getAcur() < opHealth)
                return leftBest;


            // else nothing matches
            return null;
        }

        //definitely going to be p2
        else if (root.getHMax()>opAttack&& root.getAMax()<opHealth) {//priority 2
            if (root.rightChild == null||root.rightChild.getHMax() <= opAttack) {
                Card bestHere2 = null;
                for (Card c : root.nodeLst) {
                    if (c.getHcur() > opAttack) {
                        bestHere2 = c;
                        return bestHere2;
                    }
                }
                if (root.leftChild != null)
                    return findOptimalCard(root.leftChild, opAttack, opHealth);
                return null;

            }

            if (root.rightChild != null)
                if (root.rightChild.getHMax() > opAttack) {
                    return findOptimalCard(root.rightChild, opAttack, opHealth);
                }
        }

        //definitely going to be p3
        else if (root.getHMax()<=opAttack&& root.getAMax()>=opHealth) {//priority 3
            if (root.leftChild == null || root.leftChild.getAMax() < opHealth) {
                Card bestHere2 = null;
                for (Card c : root.nodeLst) {
                    if (c.getAcur() >= opHealth) {
                        bestHere2 = c;
                        return bestHere2;
                    }
                }
                if (root.rightChild != null)
                    return findOptimalCard(root.rightChild, opAttack, opHealth);
                return null;

            }
            if (root.leftChild != null) {
                if (root.leftChild.getAMax() >= opHealth) {
                    return findOptimalCard(root.leftChild, opAttack, opHealth);
                }
            }
        }
        //definitely going to be p4
        //going directly to the rightmost child
        else if (root.getHMax()<=opAttack && root.getAMax()<opHealth){//priority4
            if (root.rightChild==null) {
                if (root.nodeLst.isEmpty()) return null;
                return root.nodeLst.get(0);
            }
            else return findOptimalCard(root.rightChild,opAttack, opHealth);
        }
        if (defaultcard == null && root.nodeLst != null && !root.nodeLst.isEmpty())
            return root.nodeLst.get(0);


        return defaultcard;
    }

    public Card findOptimalDiscardPile(Node root, int healthPoints){
        Card defaultcard=null;
        if(root==null){return null;}
        if (root.nodeLst == null || root.nodeLst.isEmpty()) return null;
        return defaultcard;
    }

    private void propagateUpwards(Node n) {
        while (n != null) {
            n.setHeight(1 + Math.max(getHeight(n.leftChild), getHeight(n.rightChild)));
            n.hMaxvalue();
            n.aMaxvalue();
            n = n.parent;

        }}
    //removeresult class because remove will return more than one thing
    public static class RemoveResult {
        Node newRoot;
        Card removedCard;
        //constructor
        public RemoveResult(Node newRoot, Card removedCard) {
            this.newRoot = newRoot;
            this.removedCard = removedCard;
        }
    }

    //removing the card
    public RemoveResult removeCard(Node root, Card c) {



        if (c == null) return new RemoveResult(root, null);
        Node n = c.belongingNode;
        if (n == null) return new RemoveResult(root, null);
        // Remove card from its node
        n.nodeLst.remove(c);
        count -= 1;
        c.belongingNode = null;
        // Recalculate node stats
        n.hMaxvalue();
        n.aMaxvalue();
        // If node is now empty, remove node itself
        if (n.nodeLst.isEmpty()) {
            root = deleteNode(root, n.getNodeAttack());
        }

        if (n == null) return null;
        propagateUpwards(n.parent);  // propagate to all parents


        // Return both updated root and removed card
        return new RemoveResult(root, c);
    }

    public Node deleteNode(Node node, int attack) {
        if (node == null) return null;

        if (node.getNodeAttack() > attack) {
            node.leftChild = deleteNode(node.leftChild, attack);
            if (node.leftChild != null) node.leftChild.parent = node; //  parent fix
        }
        else if (node.getNodeAttack() < attack) {
            node.rightChild = deleteNode(node.rightChild, attack);
            if (node.rightChild != null) node.rightChild.parent = node; //  parent fix
        }
        else { // find a match
            // Leaf case
            if (node.leftChild == null && node.rightChild == null) {
                return null;
            }
            // Only right child
            else if (node.leftChild == null) {
                Node child = node.rightChild;
                child.parent = node.parent; // update parent
                return child;
            }
            // Only left child
            else if (node.rightChild == null) {
                Node child = node.leftChild;
                child.parent = node.parent; //update parent
                return child;
            }
            // two children: inorder successor
            else {
                Node successor = node.rightChild;
                while (successor.leftChild != null) {
                    successor = successor.leftChild;
                }

                int successorAttack = successor.getNodeAttack();
                node.setNodeAttack(successorAttack);
                node.nodeLst = new ArrayList<>(successor.nodeLst);
                for (Card c : node.nodeLst) c.belongingNode = node;

                // delete the successor and connect the parent
                node.rightChild = deleteNode(node.rightChild, successorAttack);
                if (node.rightChild != null) node.rightChild.parent = node;
            }
        }

        // update
        node.setHeight(1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild)));
        node.aMaxvalue();
        node.hMaxvalue();
        propagateUpwards(node.parent); // update parents

        // --- 6️⃣ AVL dengesi sağla
        int caseN = checkBalance(node);
        switch (caseN) {
            case 1:
                node = rightRotate(node);
                break;
            case 2:
                node.leftChild = leftRotate(node.leftChild);
                node = rightRotate(node);
                break;
            case 3:
                node.rightChild = rightRotate(node.rightChild);
                node = leftRotate(node);
                break;
            case 4:
                node = leftRotate(node);
                break;
            default:
                break;
        }

        //keep the connections
        if (node.leftChild != null) node.leftChild.parent = node;
        if (node.rightChild != null) node.rightChild.parent = node;

        return node;
    }




    public Card stealCard(int attackReq,int healthReq,Node root){//finding the ideal card to steal
        if (root==null){return null;}
        Card best = null;
        //if theres no card that can surpass these values dont bother searching
        if (root.getHMax()>healthReq&&root.getAMax()>attackReq){
            if(root.getNodeAttack()>attackReq){
                for (Card c : root.nodeLst) {
                    if (c.getHcur() > healthReq) {
                        if (best == null) {
                            best = c;
                            break;
                        }

                    }
                }
            }
            Card leftBest=null;
            Card rightBest=null;
            if(root.leftChild!=null){
                if(root.leftChild.getAMax()>attackReq&&root.leftChild.getHMax()>healthReq){
                     leftBest = stealCard(attackReq, healthReq, root.leftChild);}
            }
            //ideal since min attack
            if(leftBest!=null){return leftBest;}
            //second ideal
            if(best!=null){return best;}
            if(root.rightChild!=null){
                rightBest = stealCard(attackReq, healthReq, root.rightChild);}
            if(rightBest!=null){return rightBest;}
            return null;
        }
        else{return null;}
    }

    public Node movingDiscard(Card deadCard, Node discardRoot) {
        if (deadCard == null) return discardRoot;

        deadCard.setHcur(0);
        deadCard.setRevivalProgress(0);

        discardRoot = nodeInsertingDiscardPile(discardRoot, deadCard);
        propagateUpwards(discardRoot);

        return discardRoot;
    }
    //gettng the height safely
    public int getHeight(Node n) {
        if (n==null){
            return 0;}
        else{return n.getHeight();}
    }


    public static class BattleResult {
        Node deckRoot;
        Node discardRoot;
        boolean isPlayed; // if a card was played this round
        Card playedCard;  //card that was played
        int priority;     //priority type of the played card (1–4)

        public BattleResult(Node deckRoot, Node discardRoot, boolean isPlayed, Card playedCard, int priority) {
            this.deckRoot = deckRoot;
            this.discardRoot = discardRoot;
            this.isPlayed = isPlayed;
            this.playedCard = playedCard;
            this.priority = priority;
        }
    }

    private int determinePriority(Card c, int opAttack, int opHealth) {
        if (c==null){ return 0;}
        if (c.getHcur() > opAttack && c.getAcur() >= opHealth) return 1;//survives and kills
        if (c.getHcur() > opAttack && c.getAcur() < opHealth) return 2; //survives but does not kill
        if (c.getHcur() <= opAttack && c.getAcur() >= opHealth) return 3;//dies and kills
        if  (c.getHcur() <= opAttack && c.getAcur() < opHealth) return 4;//dies and does not kill
        return 0;
    }
    public BattleResult battle(Node deckRoot,Node rootDiscard,int opAttack,int opHealth){




        Card c= findOptimalCard(deckRoot, opAttack, opHealth);

        if (c == null){
            score.strangerScore+=2;
            return new BattleResult(deckRoot, rootDiscard,false,null,0);}
        Card playedCard = c;
        int cHealth=c.getHcur();
        int cAttack=c.getAcur();
        int priority = determinePriority(c, opAttack, opHealth);

        if(priority==1){//survives and kills

            if (opAttack!=0){score.strangerScore+=1;}
            if(cAttack!=0){score.survivorScore+=2;}
            RemoveResult m = removeCard(deckRoot, c);
            deckRoot = m.newRoot;
            propagateUpwards(deckRoot);

            c=m.removedCard;
            c.setHcur(cHealth-opAttack);
            c.setAcur(max(1,(Math.floorDiv( (c.getAbase()*c.getHcur()),c.getHbase()))));
            deckRoot=nodeInsertingDeck(deckRoot,c);

            return new BattleResult(deckRoot, rootDiscard, true, playedCard, 1);
    }
        else if(priority==2){//survives but does not kill


            if (opAttack!=0){score.strangerScore+=1;}
            if(cAttack!=0){score.survivorScore+=1;}
            RemoveResult m = removeCard(deckRoot, c);
            deckRoot = m.newRoot;
            propagateUpwards(deckRoot);

            c=m.removedCard;
            c.setHcur(cHealth-opAttack);
            c.setAcur(max(1,(Math.floorDiv( (c.getAbase()*c.getHcur()),c.getHbase()))));
            deckRoot=nodeInsertingDeck(deckRoot,c);
            return new BattleResult(deckRoot, rootDiscard, true, playedCard, 2);

        }
        else if(priority==3){//dies and kills
            // remove from the deck
            RemoveResult rm = removeCard(deckRoot, c);
            deckRoot = rm.newRoot;
            score.survivorScore+=2;
            score.strangerScore+=2;
            rootDiscard = movingDiscard(c, rootDiscard);
            propagateUpwards(deckRoot);
            propagateUpwards(rootDiscard);

            return new BattleResult(deckRoot, rootDiscard, true, playedCard, 3);
        }
        else if(priority==4){//dies and does not kill
            playedCard = c;

            // remove from the deck
            RemoveResult rm = removeCard(deckRoot, c);
            deckRoot = rm.newRoot;

            if (opAttack!=0){score.strangerScore+=2;}
            if(cAttack!=0){score.survivorScore+=1;}
            rootDiscard = movingDiscard(c, rootDiscard);

            propagateUpwards(deckRoot);
            propagateUpwards(rootDiscard);


            return new BattleResult(deckRoot, rootDiscard, true, playedCard, 4);

        }


        propagateUpwards(deckRoot);
        propagateUpwards(rootDiscard);
        return new BattleResult(deckRoot, rootDiscard, false, playedCard, 0);
    }
}

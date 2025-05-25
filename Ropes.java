public class Ropes {
    public class RopeNode {
        int weight;
        int height;
        RopeNode left, right;
        String data;

        RopeNode(String data) {
            if (data != null && data.length() > 75) {
                int mid = data.length() / 2;
                RopeNode leftChild = new RopeNode(data.substring(0, mid));
                RopeNode rightChild = new RopeNode(data.substring(mid));
                this.left = leftChild;
                this.right = rightChild;
                this.data = null;
                weightAndHeightUpdater();
            } else {
                this.data = data;
                this.weight = (data != null) ? data.length() : 0;
                this.height = 1;
            }
        }


        RopeNode(RopeNode left_, RopeNode right_) {
            this.left = left_;
            this.right = right_;
            this.data = null;
            weightAndHeightUpdater();
        }

        void weightAndHeightUpdater() {
            if (isLeafNode()) {
                weight = (data != null) ? data.length() : 0;
                height = 1;
                return;
            }
            weight = (left != null) ? left.length() : 0;
            int leftHeight = (left != null) ? left.height : 0;
            int rightHeight = (right != null) ? right.height : 0;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        int length() {
            if (isLeafNode()) return (data != null) ? data.length() : 0;
            int leftLength = (left != null) ? left.length() : 0;
            int rightLength = (right != null) ? right.length() : 0;
            return leftLength + rightLength;
        }

        boolean isLeafNode() {
            return data != null;
        }

        int getBalance() {
            int leftHeight = (left != null) ? left.height : 0;
            int rightHeight = (right != null) ? right.height : 0;
            return leftHeight - rightHeight;
        }
    }

    private RopeNode root;

    public Ropes(String data) {
        this.root = new RopeNode(data);
    }

    private Ropes(RopeNode root) {
        this.root = root;
    }

    public int length() {
        return root.length();
    }

    public String toString() {
        return toString(root);
    }

    private String toString(RopeNode  nodey) {
        if ( nodey == null) return "";
        if ( nodey.isLeafNode()) return  nodey.data;
        return toString( nodey.left) + toString( nodey.right);
    }

    public String substring(int start, int end) {
        if (start >= end || start < 0 || end > length()) {
            return "";
        }
        RopeNode[] rightSplit = split(root, end);
        RopeNode[] leftSplit = split(rightSplit[0], start);
        return toString(leftSplit[1]);
    }

    public void insert(String data, int index) {
        if (index < 0 || index > length()) {
            return;
        }
        RopeNode[] splitResult = split(root, index);
        RopeNode middle = new RopeNode(data);
        RopeNode merged = concat(splitResult[0], middle);
        root = concat(merged, splitResult[1]);
    }

    public void remove(int start, int end) {
        if (start >= end || start < 0 || end > length()) {
            return;
        }
        RopeNode[] firstSplit = split(root, start);
        RopeNode[] secondSplit = split(firstSplit[1], end - start);
        root = concat(firstSplit[0], secondSplit[1]);
    }

    public Ropes concat(Ropes other) {
        return new Ropes(concat(this.root, other.root));
    }

    private RopeNode concat(RopeNode left, RopeNode right) {
        if (left == null) return right;
        if (right == null) return left;
        RopeNode newRoot = new RopeNode(left, right);
        return rebalance(newRoot);
    }

    private RopeNode[] split(RopeNode  nodey, int index) {
        if (nodey == null) return new RopeNode[]{null, null};

        if (nodey.isLeafNode()) {
            String text = nodey.data;
            if (index >= text.length()) return new RopeNode[]{nodey, null};
            if (index <= 0) return new RopeNode[]{null, nodey};
            RopeNode left = new RopeNode(text.substring(0, index));
            RopeNode right = new RopeNode(text.substring(index));
            return new RopeNode[]{left, right};
        }

        if (index < nodey.weight) {
            RopeNode[] leftSplit = split(nodey.left, index);
            RopeNode newRight = concat(leftSplit[1], nodey.right);
            return new RopeNode[]{leftSplit[0], newRight};
        } else {
            RopeNode[] rightSplit = split(nodey.right, index - nodey.weight);
            RopeNode newLeft = concat(nodey.left, rightSplit[0]);
            return new RopeNode[]{newLeft, rightSplit[1]};
        }
    }

    private RopeNode rebalance(RopeNode  nodey) {
        if ( nodey == null) return null;

         nodey.weightAndHeightUpdater();
        int balance =  nodey.getBalance();

        if (balance > 1) {
            if ( nodey.left != null &&  nodey.left.getBalance() < 0) {
                 nodey.left = rotateLeft( nodey.left);
            }
            return rotateRight( nodey);
        }

        if (balance < -1) {
            if ( nodey.right != null &&  nodey.right.getBalance() > 0) {
                 nodey.right = rotateRight( nodey.right);
            }
            return rotateLeft(nodey);
        }

        return  nodey;
    }

    private RopeNode rotateRight(RopeNode y) {
        RopeNode x = y.left;
        RopeNode  temp = x.right;

        x.right = y;
        y.left =  temp;

        y.weightAndHeightUpdater();
        x.weightAndHeightUpdater();
        return x;
    }

    private RopeNode rotateLeft(RopeNode x) {
        RopeNode y = x.right;
        RopeNode temp = y.left;

        y.left = x;
        x.right = temp;
        x.weightAndHeightUpdater();
        y.weightAndHeightUpdater();
        return y;
    }
}

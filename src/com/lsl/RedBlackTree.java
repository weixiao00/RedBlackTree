package com.lsl;

public class RedBlackTree {

    //根节点
    private RedBlackNode root;
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public class RedBlackNode {

        private int key;
        private RedBlackNode left;
        private RedBlackNode right;
        private RedBlackNode parent;
        private boolean color;

        public RedBlackNode(int key, RedBlackNode left, RedBlackNode right, RedBlackNode parent, boolean color) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.color = color;
        }

    }

    //节点左旋方法,将当前节点变为其右子树的左子树
    private void leftRotate(RedBlackNode x) {
        RedBlackNode y = x.right;//将当前节点的右子树存储起来
        x.right = y.left; //将y的左孩子赋值给x的右孩子
        if (y.left != null) { //判断y的左孩子是否存在
            y.left.parent = x;//如存在，将x变为y的左孩子的父亲
        }

        y.parent = x.parent; //将x的父亲变为y的父亲
        if (x.parent == null) {//如果x是根节点
            root = y; //将y设置成为根节点
        } else {//不是根节点，分为x是其父节点的左孩子，还是右孩子
            if (x.parent.left == x) {
                x.parent.left = y;//将y变成x的父节点的左孩子
            } else {
                x.parent.right = y;//将y变成x的父节点的右孩子
            }
        }
        y.left = x;//将变成y的左孩子
        x.parent = y;//将y变成x的父节点
    }

    //节点右旋方法，将当前节点变为其左孩子的右孩子
    private void rightRotate(RedBlackNode y) {
        RedBlackNode x = y.left; //将当前节点的左子树存储起来
        y.left = x.right; //将x的右子树变为y的左子树
        if (x.right != null) { //判断x的右子树是否存在
            x.right.parent = y; //如存在，将y变为x的右子树的父亲
        }

        x.parent = y.parent; //将y的父亲变为x的父亲
        if (y.parent == null) {//如果y是根节点
            root = x; //将x变为根节点(根节点其实就是一个引用变量,将x的引用赋值给根的引用)
        } else {
            if (y.parent.left == y) {//判断y是其父节点的左孩子，还是右孩子
                y.parent.left = x;//将x变为y的父亲的左孩子
            } else {
                y.parent.right = x;//将x变为y的父亲的右孩子
            }
        }
        x.right = y;//将y变为x的右孩子
        y.parent = x;//将x变为y的父亲
    }

    //重新调整红黑树的平衡方法
    private void insertFixUp(RedBlackNode node) {
        RedBlackNode parent, gparent, uncle;//当前节点的父亲节点和祖父节点

        while ((parent = parentOf(node)) != null && isRed(parent)) {//当前节点的父节点存在并且为红色

            gparent = parentOf(parent);//获取祖父节点
            if (gparent.left == parent) {//判断父节点是祖父节点的左孩子还是右孩子
                uncle = gparent.right;//获取祖父节点的右孩子(叔叔节点)
                //情况一:叔叔是红色(当前节点是左孩子，还是右孩子都一样)
                if (uncle != null && isRed(uncle)) {//判断叔叔节点是否为空，是否为红色
                    setRed(gparent);//设置祖父节点为红色
                    setBlack(parent);//设置父节点为黑色
                    setBlack(uncle);//设置叔叔节点为黑色
                    node = gparent;//将祖父节点变为当前节点(原因:祖父节点变为了红色，可能向上有影响)
                    continue;//执行下一次循环
                }
                //情况二:叔叔是黑色，当前节点是右孩子
                if (node == parent.right) {//此条件执行uncle为空(空节点默认就是黑色)和不为空但是为黑色
                    leftRotate(parent);
                    RedBlackNode tmp;
                    //有疑问，旋转之后为什么还要交换节点呢？？？？？？？？？
                    //应该是交换节点的引用而已
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }
                //情况三:叔叔是黑色，当前节点是左孩子
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);//祖父节点右旋
            } else {//父节点是祖父节点的右孩子
                uncle = gparent.left;//叔叔为祖父节点的左孩子
                //情况一:叔叔是红色
                if (uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }
                //情况二:叔叔是黑色，当前节点是左孩子
                if (node == parent.left) {
                    rightRotate(parent);
                    RedBlackNode tmp;
                    tmp = node;
                    node = parent;
                    parent = tmp;
                }
                //情况三:叔叔是黑色，当前节点是右孩子
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }
        setBlack(root);//将根设置为黑色
    }

    /**
     * 将节点先按照二叉查找树方法插入，设置为红色，调整红黑树的平衡
     *
     * @param node
     */
    private void insertNdoe(RedBlackNode node) {
        RedBlackNode x = root;//将根节点赋值给x
        RedBlackNode y = null;//循环最终得到的节点
        while (x != null) {//从根节点开始循环
            y = x;//保存每次循环到的节点
            int tmp = compare(node, x);
            if (tmp > 0) {
                x = x.right;
            } else {
                x = x.left;
            }
        }
        node.parent = y;//将y设置成为当前节点的父亲
        if (y == null) {
            root = node;//将当前节点的引用赋值给根
        } else {//判断将当前节点设置成为y的左子树，还是右子树
            int tmp = compare(node, y);
            if (tmp > 0) {
                y.right = node;
            } else {
                y.left = node;
            }
        }
        setRed(node);//将当前要插入的节点设置成为红色

        insertFixUp(node);//重新调整红黑树的平衡
    }

    /**
     * 删除节点方法
     * 分为三种情况
     * 1，左右子树都不为空
     * 2，左右子树都为空
     * 3，左右子树有一个为空
     */
    private void deleteNode(RedBlackNode node) {
        RedBlackNode child, parent;
        boolean color;
        if ((node.left != null) && (node.right != null)) {
            //寻找删除节点的后继结点
            RedBlackNode replace = successorNode(node);
            if (node.parent == null) {
                root = replace;
            } else if (node.parent.left == node) {
                node.parent.left = replace;
            } else {
                node.parent.right = replace;
            }

            parent = replace.parent;
            child = replace.right;
            color = replace.color;

            if (parent == node) {
//                parent = replace;
            } else {
                if (child != null) {
                    child.parent = parent;
                }
                parent.left = child;
                replace.right = node.right;
                node.right.parent = replace;
            }
            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;
            node.left.parent = replace;

            node = null;
            if (color == true) {
                deleteFixUp(child, parent);
            }
            return;
        }

        if (node.left != null) {
            child = node.left;
        } else {
            child = node.right;
        }
        parent = node.parent;
        color = node.color;
        if (parent == null) {
            root = child;
        } else if (node == parent.left) {
            parent.left = child;
            if (child != null) {
                child.parent = parent;
            }
        } else {
            parent.right = child;
            if (child != null) {
                child.parent = parent;
            }
        }

        if (color == true) {
            deleteFixUp(child, parent);
        }
        node = null;
    }

    /**
     * 删除节点后重新调整平衡
     * 分为两大类
     * 一：other为红色二①，父由黑变红，other由红变黑，父左旋，变成二①
     * ①调整为
     * 二：other为黑色
     * ①：左右孩子都为黑色，父变红，向上递归
     * ②：如果other为parent的右子树，other的右孩子是黑色（nil），左孩子是红色，other和左孩子变换颜色，other右旋，other为左子树
     *    时相反
     * ③：如果other为parent的右子树，other的右孩子是红色，左孩子任意颜色，父亲颜色给other，父变黑，other的右孩子变黑，other右旋
     *    other为左子树时相反
     * @param node
     * @param parent
     */
    private void deleteFixUp(RedBlackNode node, RedBlackNode parent) {
        RedBlackNode other;
        while ((parent != null) && ((node == null) || isBlack(node))) {
            if (parent.left == node) {
                other = parent.right;
                if (isRed(other)) {
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }

                if ((other.left == null || isBlack(other.left)) && (other.right == null || isBlack(other.right))) {
                    setRed(other);//如果other的父亲parent是红色的，直接跳出循环，附黑即可,否则，向上递归即可
                    node = parent;
                    parent = parentOf(node);
                    continue;
                }
                if ((other.right == null || isBlack(other.right)) && isRed(other.left)) {
                    setRed(other);
                    setBlack(other.right);
                    leftRotate(other);
                    other = parent.left;
                }
                if (isRed(other.left)) {
                    setBlack(other.left);
                    other.color = parent.color;
                    setBlack(parent);
                    rightRotate(parent);
                    node = root;
                    break;
                }

            } else {
                other = parent.left;
                if (isRed(other)) {
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }
                if ((other.left == null || isBlack(other.left)) && (other.right == null || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                    continue;
                }
                if ((other.left == null || isBlack(other.left)) && isRed(other.right)) {
                    setRed(other);
                    setBlack(other.right);
                    leftRotate(other);
                    other = parent.left;
                }
                if (isRed(other.left)) {
                    setBlack(other.left);
                    other.color = parent.color;
                    setBlack(parent);
                    rightRotate(parent);
                    node = root;//可能把根节点变成了红色，此时需要更爱颜色
                    break;
                }

            }
        }
        if (node != null) {
            setBlack(node);
        }
    }

    /**
     * 删除节点外部接口
     *
     * @param key
     */
    public void delete(int key) {
        RedBlackNode node;
        if ((node = search(root,key)) != null) {
            deleteNode(node);
        }else{
            System.out.println("没有值为"+key+"的元素");
        }
    }

    /**
     * 查找给定key值的节点
     *
     * @param key 给定的key值
     * @return 返回null说明没有找到
     */
    private RedBlackNode search(RedBlackNode node,int key) {
        RedBlackNode temp = new RedBlackNode(key, null, null, null, true);
        while (node != null) {
            int tmp = compare(temp, node);
            if (tmp > 0) {
                node = node.right;
                if (node == null) {
                    return null;
                }else{
                    if (node.key == key){
                        return node;
                    }
                }
            } else if (tmp < 0) {
                node = node.left;
                if (node == null) {
                    return null;
                }else {
                    if (node.key == key){
                        return node;
                    }
                }
            } else {
                return node;
            }
        }
        return null;
    }

    /**
     * 外部接口，插入实际的数据
     *
     * @param key
     */
    public void insert(int key) {
        RedBlackNode node = new RedBlackNode(key, null, null, null, true);
        if (node != null)
            insertNdoe(node);
    }

    /**
     * 返回节点的父亲节点
     *
     * @param node
     * @return
     */
    private RedBlackNode parentOf(RedBlackNode node) {
        return node.parent;
    }

    /**
     * 判断节点的颜色
     *
     * @param node
     * @return
     */
    private boolean isRed(RedBlackNode node) {
        return node.color == RED ? true : false;
    }

    /**
     * 判断节点的颜色
     *
     * @param node
     * @return
     */
    private boolean isBlack(RedBlackNode node) {
        return node.color == BLACK ? true : false;
    }

    /**
     * 设置当前节点为红色
     *
     * @param node
     */
    private void setRed(RedBlackNode node) {
        node.color = false;
    }

    /**
     * 设置当前节点为黑色
     *
     * @param node
     */
    private void setBlack(RedBlackNode node) {
        node.color = true;
    }

    /**
     * 比较两个节点的关键
     *
     * @param node
     * @param x
     * @return 返回1，node>x 返回-1，node<x
     */
    private int compare(RedBlackNode node, RedBlackNode x) {
        if (node.key > x.key) {
            return 1;
        } else if (node.key < x.key) {
            return -1;
        } else {
            return 0;
        }

    }

    /**
     * 寻找当前节点的后继结点方法
     * @param node 当前节点
     * @return 返回后继结点
     */
    private RedBlackNode successorNode(RedBlackNode node) {
        node = node.right;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 先序遍历树
     *
     * @param root
     */
    public void preorder(RedBlackNode root) {
        if (root != null) {
            System.out.println(root.key + "  " + root.color);
            preorder(root.left);
            preorder(root.right);
        }
    }

    /**
     * 中序遍历树
     *
     * @param root
     */
    public void inorder(RedBlackNode root) {
        if (root != null) {
            inorder(root.left);
            System.out.println(root.key + "  " + root.color);
            inorder(root.right);
        }
    }

    /**
     * 后序遍历树
     *
     * @param root
     */
    public void postorder(RedBlackNode root) {
        if (root != null) {
            postorder(root.left);
            postorder(root.right);
            System.out.println(root.key + "  " + root.color);
        }
    }

    public static void main(String[] args) {

        RedBlackTree tree = new RedBlackTree();
        tree.insert(10);
        tree.insert(40);
        tree.insert(30);
        tree.insert(60);
        tree.insert(90);
        tree.insert(70);
        tree.insert(20);
        tree.insert(50);
        tree.insert(80);

        tree.delete(30);
        tree.preorder(tree.root);
        System.out.println("----------------");
        tree.inorder(tree.root);
        System.out.println("----------------");
        tree.postorder(tree.root);
    }

}

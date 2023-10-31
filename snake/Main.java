package snake;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.Integer.parseInt;

public class Main extends Frame {
    //初始化蛇的位置
    Node[] snakePosition={//左边是一个Node类型的数组，右边也是一个数组（每个元素是一个Node类型的对象)
            new Node(140,100),
            new Node(120,100),
            new Node(100,100)
    };

    //初始化食物的位置
    //随机生成数字(不超过窗口的宽高）
//    public int productNum(int maxNum){
//        int n=(int)(Math.random()*maxNum)-20;
//        return n;
//    }

    public static int productNum(int min,int max){
        return  (int)(Math.random()*(max-min)+min);
    }
    //随机生成食物的坐标
    Node foodPosition=new Node(productNum(0,600/20-1)*20,productNum(2,500/20-1)*20);

    //产生食物
    public Node productFood(){
        return new Node(productNum(0,600/20-1)*20,productNum(2,500/20-1)*20);
    }

    //定义上下左右(默认向右）
    int snakeDirection=0;
    //让蛇动起来
    public void snakeMove(){
        //先默认向右
        //一个新数组中添加头元素，然后剩下的将原本的位置坐标拷贝进来放在下标为2的位置当成身体//再重新赋值给原来的数组（因为下面画的是原本的哪个变量）
        //Node newArr=new Node(snakePosition[0].getX()+20,snakePosition[0].getY()+20);
//        Node[] newArr={
//                new Node(snakePosition[0].getX()+20,snakePosition[0].getY()+20)
//        };
        /*
        * /**
         * src:原数组
         * srcPos：原数组的起始拷贝位置
         * dest：目标数组
         * destPos：目标数组的起始拷贝位置
         * length：拷贝的长度
         //3.调用arraycopy方法进行复制
        System.arraycopy(srcArr, 1, destArr, 3, 4);
         */
         //System.arraycopy(snakePosition,0,newArr,1,2);
         //snakePosition=newArr;//这样子会造成数组空间不足

        Node[] newArr=new Node[snakePosition.length];
        //newArr[0]=new Node(snakePosition[0].getX()+20,snakePosition[0].getY());
//        System.arraycopy(snakePosition,0,newArr,1,snakePosition.length-1);
//        snakePosition=newArr;


        //获取键盘
        getKeyCode();
        switch (snakeDirection){
            case 0:{
                //向右
                newArr[0]=new Node(snakePosition[0].getX()+20,snakePosition[0].getY());
                break;
            }
            case 2:{
                //向左
                newArr[0]=new Node(snakePosition[0].getX()-20,snakePosition[0].getY());
                break;
            }
            case 1:{
                //向上
                newArr[0]=new Node(snakePosition[0].getX(),snakePosition[0].getY()-20);
                break;
            }
            case 3:{
                //向下
                newArr[0]=new Node(snakePosition[0].getX(),snakePosition[0].getY()+20);
                break;
            }
        }
        System.arraycopy(snakePosition,0,newArr,1,snakePosition.length-1);
        snakePosition=newArr;

    }
public void getKeyCode(){
    //根据用户键盘移动
    addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println(e.getKeyCode());
            //w 87  d68  s83  a65
            switch (e.getKeyCode()){
                case 87:{
                    //向上
                    snakeDirection=1;
                    break;
                }
                case 83:{
                    //向下
                    snakeDirection=3;
                    break;
                }
                case 65:{
                    //向左
                    snakeDirection=2;
                    break;
                }
                case 68:{
                    //向右
                    snakeDirection=0;
                    break;
                }
            }
        }
    });
}
    //构造方法//初始化成员变量
    public Main(){
        //设置窗体位置
        setLocation(200,100);
        //设置窗口大小
        setSize(600,500);
        //设置窗口颜色
        setBackground(Color.PINK);
        //设置窗口标题
        setTitle("第一个Javese项目--贪吃蛇");

        //设置窗口程序关闭模式
        //注册窗口监听
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //退出虚拟机
                System.exit(0);
            }
        });

        //固定窗口位置和大小
        setResizable(false);
        //设置窗口可见
        setVisible(true);

        //蛇开始动
        //snakeMove();
        start();
    }

    public void paint(Graphics g){//因为会自动执行，所以我们在这里初始化画我们的蛇，我们蛇的位置以及食物的位置是会发生变化的
        //画蛇
        for(int i=0;i<snakePosition.length;i++){
            if(i==0){
                //第一个元素，证明是蛇头的位置
                g.setColor(Color.BLUE);
                g.fillOval(snakePosition[i].getX(),snakePosition[i].getY(),20-1,20-1);
            }else{
                //其他元素为蛇身
                g.setColor(Color.BLUE);
                g.fillRect(snakePosition[i].getX(),snakePosition[i].getY(),20-1,20-1);
            }
        }
        //改变蛇坐标，让蛇移动
        snakeMove();

        //画食物
        g.setColor(Color.red);
        g.fillRect(foodPosition.getX(), foodPosition.getY(),20-1,20-1);

        eatFood();
        gameOver(g);
    }
    private  boolean isRunning=true;
    public void start(){
        //开始游戏
        //因为paint在创建对象的时候会自动执行一次，过后不再，而我们需要重新绘制，让蛇一次一次动起来
        //就需要调用父类的repaint方法
        while(isRunning){//执行之后就重新绘制窗口了（变成了啥也没有）
            super.repaint();
            //System.out.println("重绘");
            try{
                Thread.sleep(500);//暂停事件
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    //吃食物
    public void eatFood(){
        //蛇吃到食物的判断
        if(snakePosition[0].getX()==foodPosition.getX()&&snakePosition[0].getY()== foodPosition.getY()){
            //蛇吃到食物啦
            //增长蛇的长度
            Node[] newArr=new Node[snakePosition.length+1];
            newArr[0]=new Node(foodPosition.getX(), foodPosition.getY());
            System.arraycopy(snakePosition,0,newArr,1,snakePosition.length);
            snakePosition=newArr;

            //食物消失
            foodPosition=null;
            //产生新的食物
            foodPosition=productFood();
        }

    }
    //蛇碰到边界死
    public void gameOver(Graphics g){
        //四个边界
        if(snakePosition[0].getX()>=600-20||snakePosition[0].getX()<20||snakePosition[0].getY()>=500-20||snakePosition[0].getY()<=20){
            isRunning=false;//停止运动
            //画出gameOver
            g.setFont(new Font(null,Font.ITALIC,100));
            g.drawString("游戏结束",150,300);
        }
    }
    public static void main(String[] args) {
        new Main();//次类继承了Frame，创建的时候可以调用父类的方法

        //初始化我们蛇的坐标
        //测试
//        Node n=new Node();
//        n.setX(1);
//        System.out.println(n.getY());

    }
}

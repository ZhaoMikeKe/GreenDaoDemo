package com.example.greendaodemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.greendaodemo.demo.Card;
import com.example.greendaodemo.demo.Course;
import com.example.greendaodemo.demo.JoinTeacherWithCourse;
import com.example.greendaodemo.demo.MyGreenDAOApplication;
import com.example.greendaodemo.demo.Orders;
import com.example.greendaodemo.demo.Teacher;
import com.example.greendaodemo.demo.User;
import com.example.greendaodemo.greendao.gen.CardDao;
import com.example.greendaodemo.greendao.gen.CourseDao;
import com.example.greendaodemo.greendao.gen.JoinTeacherWithCourseDao;
import com.example.greendaodemo.greendao.gen.OrdersDao;
import com.example.greendaodemo.greendao.gen.TeacherDao;
import com.example.greendaodemo.greendao.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private UserDao userDao;
    private CardDao cardDao;
    private OrdersDao ordersDao;
    private JoinTeacherWithCourseDao teacherWithCourseDao;
    private CourseDao courseDao;
    private TeacherDao teacherDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取UserDao对象
        userDao = MyGreenDAOApplication.getInstances().getmDaoSession().getUserDao();
        cardDao = MyGreenDAOApplication.getInstances().getmDaoSession().getCardDao();
        ordersDao = MyGreenDAOApplication.getInstances().getmDaoSession().getOrdersDao();
        teacherDao = MyGreenDAOApplication.getInstances().getmDaoSession().getTeacherDao();
        courseDao = MyGreenDAOApplication.getInstances().getmDaoSession().getCourseDao();
        teacherWithCourseDao = MyGreenDAOApplication.getInstances().getmDaoSession().getJoinTeacherWithCourseDao();
        // onCreate()方法的代码更改如下
        userDao.deleteAll();// 保持表中的数据记录纯净，在此便于演示；按需添加

        // 依次调用
        //migrationTest();
        //migrationQueryList();

        /* insertManyToMany();
       多对多
        queryManyToManyC();
        queryManyToManyT();*/

       /* insertMany();
        insertOneToMany();
        一对多
        queryToManyUserToOrder();*/

       /* insertCardOneTOne();
       双向
        queryCardOneToOne();*/

       /* insertOneToOne();
       单向
        queryOneToOne();*/

        /*insertMany();
        updateUser();
        queryList();*/
    }

    private void migrationTest() {
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        user1.setSex("男");
        userDao.update(user1);
    }

    private void migrationQueryList() {
        String result = "显示结果为：";
        List<User> users = userDao.loadAll();
        int i = 0;
        for (User user : users) {
            i++;
            String res = result + "i=" + i + ",id:" + user.getId() + ",name:" + user.getName() +
                    ",address:" + user.getUserAddress() +
                    ",sex:" + user.getSex();
            Log.d("TAG", res);
        }
    }

    // 多对多插入
    private void insertManyToMany() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setName("英语");

        Course course2 = new Course();
        course2.setName("语文");

        Course course3 = new Course();
        course3.setName("数学");

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courseDao.insertInTx(courses);

        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setName("孙悟空");

        Teacher teacher2 = new Teacher();
        teacher2.setName("猪八戒");

        Teacher teacher3 = new Teacher();
        teacher3.setName("沙和尚");

        teacherList.add(teacher1);
        teacherList.add(teacher2);
        teacherList.add(teacher3);
        teacherDao.insertInTx(teacherList);

        List<JoinTeacherWithCourse> teacherWithCourses = new ArrayList<>();
        // 悟空教英语
        JoinTeacherWithCourse teacherWithCourse1 = new JoinTeacherWithCourse();
        teacherWithCourse1.setTId(teacher1.getId());
        teacherWithCourse1.setCId(course1.getId());

        // 悟空叫语文
        JoinTeacherWithCourse teacherWithCourse2 = new JoinTeacherWithCourse();
        teacherWithCourse2.setTId(teacher1.getId());
        teacherWithCourse2.setCId(course2.getId());

        // 悟空叫数学
        JoinTeacherWithCourse teacherWithCourse3 = new JoinTeacherWithCourse();
        teacherWithCourse3.setTId(teacher1.getId());
        teacherWithCourse3.setCId(course3.getId());

        // 沙和尚教语文
        JoinTeacherWithCourse teacherWithCourse4 = new JoinTeacherWithCourse();
        teacherWithCourse4.setTId(teacher2.getId());
        teacherWithCourse4.setCId(course2.getId());

        teacherWithCourses.add(teacherWithCourse1);
        teacherWithCourses.add(teacherWithCourse2);
        teacherWithCourses.add(teacherWithCourse3);
        teacherWithCourses.add(teacherWithCourse4);
        teacherWithCourseDao.insertInTx(teacherWithCourses);
    }

    // 多对多查询,通过”教师“找到课程
    private void queryManyToManyT() {
        Teacher teacher = teacherDao.queryBuilder().where(TeacherDao.Properties.Name.eq("孙悟空"))
                .build().unique();
        List<Course> courses = teacher.getCourses();

        if (courses != null) {
            Log.d("TAG", "孙悟空所教的课程：");
            for (Course course : courses) {
                Log.d("TAG", "课程名：" + course.getName());
            }
        }
    }

    // 多对多查询,通过”课程“找到课程
    private void queryManyToManyC() {
        Course course = courseDao.queryBuilder().where(CourseDao.Properties.Name.eq("语文"))
                .build().unique();
        List<Teacher> teachers = course.getTeachers();

        if (teachers != null) {
            Log.d("TAG", "教语文的老师有：");
            for (Teacher teacher : teachers) {
                Log.d("TAG", "教师名：" + teacher.getName());
            }
        }
    }

    private void insertOneToMany() {
        List<Orders> orderList = new ArrayList<Orders>();
        // 这些数据的来源请参考上一章所讲的内容，因为在上一章中有方法为测试提供数据源
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        User user2 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("猪八戒")).build().unique();

        Orders order1 = new Orders();
        order1.setGoodsName("金箍棒");
        order1.setUser(user1); //设置外键值时，要用setUser()方法，以确保外键值不会出错

        Orders order2 = new Orders();
        order2.setGoodsName("黄金甲");
        order2.setUser(user1);

        Orders order3 = new Orders();
        order3.setGoodsName("紫金冠");
        order3.setUser(user1);

        Orders order4 = new Orders();
        order4.setGoodsName("紫金冠");
        order4.setUser(user2);

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ordersDao.insertInTx(orderList);
    }

    private void queryToManyUserToOrder() {
        List<Orders> ordersList;
        User user1 = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();

        //直接通过User对象的getOrders()方法获得此用户的所有订单
        ordersList = user1.getOrders();
        Log.d("TAG", user1.getName() + "的订单内容为：");

        int i = 0;
        if (ordersList != null) {
            for (Orders order : ordersList) {
                i = i + 1;
                Log.d("TAG", "第" + i + "条订单的结果：" + ",id:" + order.getId()
                        + ",商品名：" + order.getGoodsName()
                        + ",用户名：" + user1.getName());
            }
        }
    }


    // 单向关联查询
    private void queryOneToOne() {
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        Card card = user.getCard();
        if (user != null && card != null) {
            Log.d("TAG", "一对一添加记录，查询后的结果是：\n" + "姓名：" + user.getName()
                    + "\n身份证号" + card.getCardCode() + "\n"
                    + "Card表的id主键值为：" + card.getId() + "\n"
                    + "User表的外键cardId的值为：" + user.getCardId());
        }
    }

    //双向关联查询
    private void queryCardOneToOne() {
        Card card1 = cardDao.queryBuilder().where(CardDao.Properties.CardCode.eq("434377777")).build().unique();
        User user1 = card1.getUser();
        if (user1 != null && card1 != null) {
            Log.d("TAG", "姓名：" + user1.getName() + "\n"
                    + "Card表的id主键值：" + card1.getId() + "\n"
                    + "User表的外键cardId的值为：" + user1.getCardId());
        }
    }

    // 单向关联插入
    private void insertOneToOne() {
        // 先生成一条Card记录
        Card card1 = new Card();
        card1.setCardCode("434377777");
        cardDao.insert(card1);

        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUserAddress("花果山水帘洞");
        user1.setUsercode("001");
        user1.setCard(card1);
        userDao.insert(user1);
    }

    //双向关联
    private void insertCardOneTOne() {
        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUserAddress("花果山水帘洞");
        user1.setUsercode("001");

        Card card1 = new Card();
        card1.setCardCode("434377777");

        /* 注意以下代码的顺序 */
        userDao.insert(user1);
        card1.setUser(user1);

        cardDao.insert(card1);
        //补上之前没有设置的user1的外键值
        user1.setCard(card1);
        //更新user1对象
        userDao.update(user1);

    }

    // 打印数据库表中的记录
    private void queryList() {
        String result = "显示结果为：";
        // 获得表中的所有记录
        List<User> users = userDao.loadAll();
        int i = 0;
        for (User user : users) {
            i++;
            String res = result + "i=" + i + ",id:" + user.getId() + ",name:" + user.getName() + ",address:" + user.getUserAddress();
            Log.d("TAG", res);
        }
    }

    private void insertMany() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setName("孙悟空");
        user1.setUsercode("001");
        user1.setUserAddress("花果山水帘洞");

        User user2 = new User();
        user2.setName("猪八戒");
        user2.setUsercode("002");
        user2.setUserAddress("高老庄");

        User user3 = new User();
        user3.setName("沙悟净");
        user3.setUsercode("003");
        user3.setUserAddress("流沙河");

        User user4 = new User();
        user4.setName("黑熊怪");
        user4.setUsercode("004");
        user4.setUserAddress("黑风山");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        userDao.insertInTx(users);
    }

    private void updateUser() {
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("孙悟空")).build().unique();
        // 修改使用setter方法
        user.setUserAddress("天宫蟠桃园");
        userDao.update(user);
    }

}

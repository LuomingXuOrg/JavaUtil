
import Model.testModel;
import Model.testModel0;
import Util.ConvertorHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class Main
{
    public static void main(String[] args)
    {
        Random rd = new Random(1000);
        List<testModel> testModels = new ArrayList<>();
        testModel temp;

        Date date = new Date();
        System.err.println(date.getClass().getName());
        System.err.println("now : " + new Date(System.currentTimeMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 100; i++)
        {
            temp = new testModel();
            temp.setDoubleTemp(rd.nextDouble());
            //temp.setIntTemp(rd.nextInt());
            temp.setLongTemp(rd.nextLong());
            temp.setDateTemp(new Date(System.currentTimeMillis() + rd.nextInt()));
            temp.setTestModelTemp(new testModel(123L, 12.3));
            testModels.add(temp);
        }
//        System.err.println("-------------normal-------------");
//        testModels.forEach(System.err::println);
//        System.err.println("-------------after-------------");
//        System.err.println(ListPageHelper.doPage(new PageRequest(3, 7, new Sort("intTemp", Sort.Direction.ASC)), testModels));
//        testModels.forEach(System.err::println);

        int count = 0;
        for (int i = 0; i < 50; i++)
        {
            long s = System.currentTimeMillis();
            List<testModel0> model0s = ConvertorHelper.convertList(new testModel0(), testModels);
            long f = System.currentTimeMillis();
            count += (f - s);
            model0s.forEach(System.out::println);
            //System.out.println(model0s.size());
        }

        System.out.println("count: " + count/50);
    }
}
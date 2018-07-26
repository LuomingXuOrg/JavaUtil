import Annotation.ListSortDefault;
import Model.PageRequest;
import Model.Sort;
import Model.testModel;
import Util.ListPageHelperUtil;
import Util.SortUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        Random rd = new Random(1000);
        List<testModel> testModels = new ArrayList<>();
        testModel temp;
        for (int i = 0; i < 20; i++)
        {
            temp = new testModel();
            temp.setNumber(rd.nextInt());
            temp.setId(rd.nextInt());
            testModels.add(temp);
        }

        SortUtil.doSort(new Sort(), testModels);
        System.err.println("--------------------------");
        for (Object item : testModels)
        {
            System.err.println(item);
        }
    }
}
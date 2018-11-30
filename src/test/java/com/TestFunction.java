package com;

import com.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ll
 * @Date 2018/11/16 10:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-solr.xml")
public class TestFunction {
    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void add() {
        TbItem item = new TbItem();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void get() {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        System.out.println(item.toString());
    }

    @Test
    public void update() {
        TbItem item = solrTemplate.getById(1, TbItem.class);
        item.setBrand("小米1");
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }

    @Test
    public void del() {
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    @Test
    public void addlist() {
        List<TbItem> list = new ArrayList();

        for (int i = 0; i < 100; i++) {
            TbItem item = new TbItem();
            item.setId(i + 1L);
            item.setBrand("华为");
            item.setCategory("手机");
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店");
            item.setTitle("华为Mate" + i);
            item.setPrice(new BigDecimal(2000 + i));
            list.add(item);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Test
    public void testPageQuery() {
        Query query = new SimpleQuery("*:*");
        query.setOffset(20);//开始索引
        query.setRows(10);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        showList(tbItems.getContent());
    }

    //显示记录数据
    private void showList(List<TbItem> list) {
        for (TbItem item : list) {
            System.out.println(item.getTitle() + "   " + item.getPrice());
        }
    }

    @Test
    public void testPageQueryMutil() {
        Query query = new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_title").contains("5");
//        criteria=criteria.and("item_title").contains("5");
        query.addCriteria(criteria);
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);
        showList(tbItems.getContent());
    }

    @Test
    public void delAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}

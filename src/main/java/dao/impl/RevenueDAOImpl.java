/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.impl;

import dao.RevenueDAO;
import entity.Revenue.ByTao;
import java.util.Date;
import java.util.List;
import util.XQuery;

/**
 *
 * @author hungddv
 */
public class RevenueDAOImpl implements RevenueDAO {

    @Override
    public List<ByTao> getByCategory(Date begin, Date end, String categoryId) {
        String revenueByCategorySql
                = "SELECT \n"
                + "    detail.id AS id,\n"
                + "    bill.Sdt AS sdt,\n"
                + "    cate.name AS category,\n"
                + "    product.name AS productName,\n"
                + "    MIN(bill.Checkin) AS Checkin,"
                + " MAX(bill.Checkout) AS Checkout,"
                + "    SUM(detail.quantity) AS quantity,\n"
                + "    CAST(SUM(detail.unitprice * detail.quantity * (1 - product.discount / 100.0)) AS FLOAT) AS revenue\n"
                + "FROM BillDetails detail\n"
                + "JOIN Bills bill ON bill.Id = detail.BillId\n"
                + "JOIN Products product ON product.Id = detail.ProductId\n"
                + "JOIN Categories cate ON cate.Id = product.CategoryId\n"
                + "WHERE bill.Status = 1\n"
                + "  AND bill.Checkout IS NOT NULL\n"
                + "  AND bill.Checkout BETWEEN ? AND ?\n"
                + "  AND cate.id = ?\n"
                + "GROUP BY \n"
                + "    detail.id, bill.Sdt, cate.name, product.name\n"
                + "ORDER BY revenue DESC;";
        return XQuery.getBeanList(ByTao.class, revenueByCategorySql, begin, end, categoryId);
    }

    @Override
    public List<ByTao> getByName(Date begin, Date end, String productName) {
        String revenueByNameSql
                = "SELECT \n"
                + "    detail.id AS id,\n"
                + "    bill.Sdt AS sdt,\n"
                + "    cate.name AS category,\n"
                + "    product.name AS productName,\n"
                + "    MIN(bill.Checkin) AS Checkin,"
                + " MAX(bill.Checkin) AS Checkout, "
                + "    SUM(detail.quantity) AS quantity,\n"
                + "    CAST(SUM(detail.unitprice * detail.quantity * (1 - product.discount / 100.0)) AS FLOAT) AS revenue\n"
                + "FROM BillDetails detail\n"
                + "JOIN Bills bill ON bill.Id = detail.BillId\n"
                + "JOIN Products product ON product.Id = detail.ProductId\n"
                + "JOIN Categories cate ON cate.Id = product.CategoryId\n"
                + "WHERE bill.Status = 1\n"
                + "  AND bill.Checkout IS NOT NULL\n"
                + "  AND bill.Checkout BETWEEN ? AND ?\n"
                + "  AND product.name = ?\n"
                + "GROUP BY \n"
                + "    detail.id, bill.Sdt, cate.name, product.name\n"
                + "ORDER BY revenue DESC;";
        return XQuery.getBeanList(ByTao.class, revenueByNameSql, begin, end, productName);
    }

    @Override
    public List<ByTao> getByTao(Date begin, Date end) {
        String revenueByNameSql
                = "SELECT detail.id AS id, "
                + " CAST(SUM(detail.unitprice * detail.quantity * (1 - product.discount / 100.0)) AS FLOAT) AS revenue,"
                + " sum(detail.Quantity) AS Quantity,"
                + " MIN(bill.Checkin) AS Checkin,"
                + " MAX(bill.Checkout) AS Checkout,"
                + " bill.Sdt AS sdt,"
                + " cate.name AS category,"
                + " product.name AS productName "
                + " FROM BillDetails detail "
                + " JOIN Products product ON product.Id=detail.productId"
                + " JOIN Categories cate ON cate.Id=product.CategoryId"
                + " JOIN Bills bill ON bill.Id=detail.BillId "
                + " WHERE bill.Status=1 "
                + " AND bill.Checkout IS NOT NULL "
                + " AND bill.Checkout BETWEEN ? AND ? "
                + " GROUP BY detail.id, bill.Sdt, cate.name, product.name"
                + " ORDER BY Revenue DESC";
        return XQuery.getBeanList(ByTao.class, revenueByNameSql, begin, end);
    }

    @Override
    public List<ByTao> getByNameAndCategory(Date begin, Date end, String productName, String categoryId) {
        String revenueByUserSql
                = "SELECT \n"
                + "    detail.id AS id,\n"
                + "    bill.Sdt AS sdt,\n"
                + "    cate.name AS category,\n"
                + "    product.name AS productName,\n"
                + "    MIN(bill.Checkin) AS Checkin,"
                + " MAX(bill.Checkout) AS Checkout,"
                + "    SUM(detail.quantity) AS quantity,\n"
                + "    CAST(SUM(detail.unitprice * detail.quantity * (1 - product.discount / 100.0)) AS FLOAT) AS revenue\n"
                + "FROM BillDetails detail\n"
                + "JOIN Bills bill ON bill.Id = detail.BillId\n"
                + "JOIN Products product ON product.Id = detail.ProductId\n"
                + "JOIN Categories cate ON cate.Id = product.CategoryId\n"
                + "WHERE bill.Status = 1\n"
                + "  AND bill.Checkout IS NOT NULL\n"
                + "  AND bill.Checkout BETWEEN ? AND ?\n"
                + "  AND product.name = ?   \n"
                + "  AND cate.id = ?       \n"
                + "GROUP BY \n"
                + "    detail.id, bill.Sdt, cate.name, product.name\n"
                + "ORDER BY revenue DESC;";
        return XQuery.getBeanList(ByTao.class, revenueByUserSql, begin, end, productName, categoryId);
    }
}

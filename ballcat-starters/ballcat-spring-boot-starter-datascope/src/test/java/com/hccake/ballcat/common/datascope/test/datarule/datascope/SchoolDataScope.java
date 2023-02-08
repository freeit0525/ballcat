package com.hccake.ballcat.common.datascope.test.datarule.datascope;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUser;
import com.hccake.ballcat.common.datascope.test.datarule.user.LoginUserHolder;
import com.hccake.ballcat.common.datascope.test.datarule.user.UserRoleType;
import com.hccake.ballcat.common.datascope.util.CollectionUtils;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 学校维度的数据权限控制
 *
 * @author hccake
 */
public class SchoolDataScope implements DataScope {

	public static final String RESOURCE_NAME = "school";

	final String columnId = "school_name";

	@Override
	public String getResource() {
		return RESOURCE_NAME;
	}

	@Override
	public boolean includes(String tableName) {
		// 可以利用表前缀做匹配
		return tableName.toLowerCase().startsWith("h2student");
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		LoginUser loginUser = LoginUserHolder.get();

		// 如果当前登录用户为空，或者是老师，但是没有任何学校权限
		if (loginUser == null || (UserRoleType.TEACHER.equals(loginUser.getUserRoleType())
				&& CollectionUtils.isEmpty(loginUser.getSchoolNameList()))) {
			// where 1 = 2 永不满足
			return new EqualsTo(new LongValue(1), new LongValue(2));
		}

		// 如果是学生，则不控制，因为学生的权限会在 StudentDataScope 中处理
		if (UserRoleType.STUDENT.equals(loginUser.getUserRoleType())) {
			return null;
		}

		// 提取当前登录用户拥有的学校权限
		List<Expression> list = loginUser.getSchoolNameList().stream().map(StringValue::new)
				.collect(Collectors.toList());
		Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
		ExpressionList expressionList = new ExpressionList();
		expressionList.setExpressions(list);
		return new InExpression(column, expressionList);
	}

}

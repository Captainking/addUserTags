package cn.edu.bjtu.weibo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserTags {

	public void setUserTags() {

		UserDAO userDao = new UserDAOImpl();
		RecommendDAO recommendDao = new RecommendDAOImpl();
		List<String> useridList = userDao.getTotalUserId();// 所有的用户的id
		List<String> sendWeiboidList = new ArrayList<String>();
		List<String> likeWeiboidList = new ArrayList<String>();
		List<String> commentWeiboidList = new ArrayList<String>();

		for (String userid : useridList) {// 遍历用户
			sendWeiboidList.addAll(userDao.getWeibo(userid, 1, userDao.getWeiboNumber(userid)));// 用户所有发送的微博的id
			Map<String, Integer> sendweibo = getTotal(sendWeiboidList);

			likeWeiboidList.addAll(userDao.getLikeWeibo(userid, 1, 100));// 用户点赞的一百条微博的id
			Map<String, Integer> likeweibo = getTotal(likeWeiboidList);

			commentWeiboidList.addAll(userDao.getCommentOnWeibo(userid, 1, 100));// 用户评论的一百条微博的id
			Map<String, Integer> commentweibo = getTotal(commentWeiboidList);

			double economic = sendweibo.get("经济") * 5 + likeweibo.get("经济") * 2.5 + commentweibo.get("经济") * 2.5;// 经济标签
			double military = sendweibo.get("军事") * 5 + likeweibo.get("军事") * 2.5 + commentweibo.get("军事") * 2.5;// 军事标签
			double car = sendweibo.get("汽车") * 5 + likeweibo.get("汽车") * 2.5 + commentweibo.get("汽车") * 2.5;// 汽车
			double sports = sendweibo.get("体育") * 5 + likeweibo.get("体育") * 2.5 + commentweibo.get("体育") * 2.5;// 体育
			double culture = sendweibo.get("文化") * 5 + likeweibo.get("文化") * 2.5 + commentweibo.get("文化") * 2.5;// 文化
			double medicine = sendweibo.get("医药") * 5 + likeweibo.get("医药") * 2.5 + commentweibo.get("医药") * 2.5;// 医药
			double total = economic + military + car + sports + culture + medicine;
			Map<String, Double> userLable = new HashMap<String, Double>();
			if (total > 0) {
				userLable.put("经济", economic / total);
				userLable.put("军事", military / total);
				userLable.put("汽车", car / total);
				userLable.put("体育", sports / total);
				userLable.put("文化", culture / total);
				userLable.put("医药", medicine / total);
			} else {
				userLable.put("经济", 0.0);
				userLable.put("军事", 0.0);
				userLable.put("汽车", 0.0);
				userLable.put("体育", 0.0);
				userLable.put("文化", 0.0);
				userLable.put("医药", 0.0);
			}
			recommendDao.setUserLabels(userid, userLable);// 写进用户标签
		}

	}

	protected Map<String, Integer> getTotal(List<String> weiboidlist) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		RecommendDAO recommendDAO = new RecommendDAOImpl();

		int economic = 0;// 经济标签
		int military = 0;// 军事标签
		int car = 0;// 汽车
		int sports = 0;// 体育
		int culture = 0;// 文化
		int medicine = 0;// 医药
		for (String weiboid : weiboidlist) {
			if (weiboid != null) {
				for (String weibolable : recommendDAO.getWeiboLabels(weiboid)) {// 每条微博的标签
					switch (weibolable) {
					case "经济":
						economic++;
						break;
					case "军事":
						military++;
						break;
					case "汽车":
						car++;
						break;
					case "体育":
						sports++;
						break;
					case "文化":
						culture++;
						break;
					case "医药":
						medicine++;
						break;
					}
				}
			}
		}
		map.put("经济", economic);
		map.put("军事", military);
		map.put("汽车", car);
		map.put("体育", sports);
		map.put("文化", culture);
		map.put("医药", medicine);
		return map;
	}

}

package cn.edu.bjtu.weibo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUserTags {

	public void setUserTags() {

		UserDAO userDao = new UserDAOImpl();
		RecommendDAO recommendDao = new RecommendDAOImpl();
		List<String> useridList = userDao.getTotalUserId();// ���е��û���id
		List<String> sendWeiboidList = new ArrayList<String>();
		List<String> likeWeiboidList = new ArrayList<String>();
		List<String> commentWeiboidList = new ArrayList<String>();

		for (String userid : useridList) {// �����û�
			sendWeiboidList.addAll(userDao.getWeibo(userid, 1, userDao.getWeiboNumber(userid)));// �û����з��͵�΢����id
			Map<String, Integer> sendweibo = getTotal(sendWeiboidList);

			likeWeiboidList.addAll(userDao.getLikeWeibo(userid, 1, 100));// �û����޵�һ����΢����id
			Map<String, Integer> likeweibo = getTotal(likeWeiboidList);

			commentWeiboidList.addAll(userDao.getCommentOnWeibo(userid, 1, 100));// �û����۵�һ����΢����id
			Map<String, Integer> commentweibo = getTotal(commentWeiboidList);

			double economic = sendweibo.get("����") * 5 + likeweibo.get("����") * 2.5 + commentweibo.get("����") * 2.5;// ���ñ�ǩ
			double military = sendweibo.get("����") * 5 + likeweibo.get("����") * 2.5 + commentweibo.get("����") * 2.5;// ���±�ǩ
			double car = sendweibo.get("����") * 5 + likeweibo.get("����") * 2.5 + commentweibo.get("����") * 2.5;// ����
			double sports = sendweibo.get("����") * 5 + likeweibo.get("����") * 2.5 + commentweibo.get("����") * 2.5;// ����
			double culture = sendweibo.get("�Ļ�") * 5 + likeweibo.get("�Ļ�") * 2.5 + commentweibo.get("�Ļ�") * 2.5;// �Ļ�
			double medicine = sendweibo.get("ҽҩ") * 5 + likeweibo.get("ҽҩ") * 2.5 + commentweibo.get("ҽҩ") * 2.5;// ҽҩ
			double total = economic + military + car + sports + culture + medicine;
			Map<String, Double> userLable = new HashMap<String, Double>();
			if (total > 0) {
				userLable.put("����", economic / total);
				userLable.put("����", military / total);
				userLable.put("����", car / total);
				userLable.put("����", sports / total);
				userLable.put("�Ļ�", culture / total);
				userLable.put("ҽҩ", medicine / total);
			} else {
				userLable.put("����", 0.0);
				userLable.put("����", 0.0);
				userLable.put("����", 0.0);
				userLable.put("����", 0.0);
				userLable.put("�Ļ�", 0.0);
				userLable.put("ҽҩ", 0.0);
			}
			recommendDao.setUserLabels(userid, userLable);// д���û���ǩ
		}

	}

	protected Map<String, Integer> getTotal(List<String> weiboidlist) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		RecommendDAO recommendDAO = new RecommendDAOImpl();

		int economic = 0;// ���ñ�ǩ
		int military = 0;// ���±�ǩ
		int car = 0;// ����
		int sports = 0;// ����
		int culture = 0;// �Ļ�
		int medicine = 0;// ҽҩ
		for (String weiboid : weiboidlist) {
			if (weiboid != null) {
				for (String weibolable : recommendDAO.getWeiboLabels(weiboid)) {// ÿ��΢���ı�ǩ
					switch (weibolable) {
					case "����":
						economic++;
						break;
					case "����":
						military++;
						break;
					case "����":
						car++;
						break;
					case "����":
						sports++;
						break;
					case "�Ļ�":
						culture++;
						break;
					case "ҽҩ":
						medicine++;
						break;
					}
				}
			}
		}
		map.put("����", economic);
		map.put("����", military);
		map.put("����", car);
		map.put("����", sports);
		map.put("�Ļ�", culture);
		map.put("ҽҩ", medicine);
		return map;
	}

}

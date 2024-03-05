package fun.qxfly.service.Admin;

import fun.qxfly.entity.Classify;
import fun.qxfly.entity.Tag;

public interface ClassifyTagManageService {

    /**
     * 删除分类
     *
     * @param classify
     * @return
     */
    boolean deleteClassify(Classify classify);

    /**
     * 添加分类
     *
     * @param classify
     * @return
     */
    boolean addClassify(Classify classify);

    /**
     * 删除标签
     *
     * @param tag
     * @return
     */
    boolean deleteTag(Tag tag);

    /**
     * 添加标签
     *
     * @param tag
     * @return
     */
    boolean addTag(Tag tag);

    /**
     * 更新分类
     * @param classify
     * @return
     */
    boolean updateClassify(Classify classify);

    /**
     * 更新标签
     * @param tag
     * @return
     */
    boolean updateTag(Tag tag);
}

/**
 * 从 itemElements 中抽离出所有的 img 元素
 */
export const getImgElements = (itemElements) => {
  const imgElements = [];
  itemElements.forEach((el) => {
    imgElements.push(...Array.from(el.getElementsByTagName("img")));
  });
  return imgElements;
};

/**
 * 生成所有的图片链接数组
 */
export const getAllImg = (imgElements) => {
  return imgElements.map((imgElement) => imgElement.src);
};

/**
 * 监听图片数组加载完成（通过 promise 完成）
 */
export const onComplateImgs = (imgs) => {
  const promiseAll = imgs.map((img, index) => {
    return new Promise((resolve) => {
      const imageObj = new Image();
      imageObj.src = img;
      imageObj.onload = () => {
        resolve({ img, index });
      };
    });
  });
  return Promise.all(promiseAll);
};

/**
 * 返回列高对象中的最小高度所在的列
 */
export const getMinHeightColumn = (columnHeightObj) => {
  const minHeight = getMinHeight(columnHeightObj);
  return Object.keys(columnHeightObj).find(
    (key) => columnHeightObj[key] === minHeight
  );
};

/**
 * 返回列高对象中的最小的高度
 */
export const getMinHeight = (columnHeightObj) => {
  const columnHeightArr = Object.values(columnHeightObj);
  return Math.min(...columnHeightArr);
};

/**
 * 返回列高对象中的最大的高度
 */
export const getMaxHeight = (columnHeightObj) => {
  const columnHeightArr = Object.values(columnHeightObj);
  return Math.max(...columnHeightArr);
};

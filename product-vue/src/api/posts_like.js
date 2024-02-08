import request from '@/utils/request'


export const addLike = (postsId, deleted) => {
    return request({
        url: '/api/postsLike/add',
        method: 'get',
        params: {
            postsId: postsId,
            deleted: deleted
        }
    })
}
/**
 * 点赞
 */
export const like = (postsId) => {
    return request({
        url: '/api/postsLike/like',
        method: 'get',
        params: {
            postsId: postsId
        }
    })
}
/**
 * 取消点赞
 */
export const unLike = (postsId) => {
    return request({
        url: '/api/postsLike/dislike',
        method: 'get',
        params: {
            postsId: postsId
        }
    })
}
/**
 * 查询
 */
export const getPostLike = (userId) => {
    return request({
        url: '/api/postsLike/get',
        method: 'get',
        params: {
            userId: userId
        }
    })
}

package org.study.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.community.domain.posts.Posts;
import org.study.community.domain.posts.PostsRepository;
import org.study.community.web.dto.PostsListResponseDto;
import org.study.community.web.dto.PostsResponseDto;
import org.study.community.web.dto.PostsSaveRequestDto;
import org.study.community.web.dto.PostsUpdateRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private static final int BLOCK_PAGE_NUM_COUNT = 5;
    private static final int PAGE_POST_COUNT = 4;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id= "+ id));

        posts.update(requestDto.getTitle(),requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }


    //paging
    @Transactional
    public List<PostsListResponseDto> getPageList(Integer pageNum){
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(pageNum -1 , PAGE_POST_COUNT, sort);
        return postsRepository.findAll(pageable).stream()
                .map(PostsListResponseDto::new).collect(Collectors.toList());
    }


    @Transactional
    public Integer getPrev(Integer pageNum){
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(pageNum -1 , PAGE_POST_COUNT, sort);

        return pageable.previousOrFirst().getPageNumber();
    }

    @Transactional
    public Integer getNext(Integer pageNum){
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(pageNum -1 , PAGE_POST_COUNT, sort);

        return pageable.next().getPageNumber();
    }

    //searching
    @Transactional
    public List<PostsListResponseDto> searchPosts(String keyword, Integer pageNum) {
        Sort sort = Sort.by(Sort.Order.desc("createdDate"));
        Pageable pageable = PageRequest.of(pageNum -1 , PAGE_POST_COUNT, sort);
        return postsRepository.findByTitleContaining(keyword, pageable).stream()
                .map(PostsListResponseDto::new).collect(Collectors.toList());

    }


    @Transactional
    public void delete (Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(()  ->  new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
    }


    public List<Integer> getPageNum(Integer curPageNum) {
        List<Integer> pageList = new ArrayList<>();

        //total posts count
        Double postsTotalCount = Double.valueOf(this.getPostsCount());

        //last page num based on total count
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        //get last page num based on current page
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;
        //set start page Num
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        //set PageNum
        for(int val =curPageNum, idx =0; val <= blockLastPageNum; val++, idx++){
            pageList.add(val);
        }
        return pageList;
    }

    @Transactional
    public Long getPostsCount(){
        return postsRepository.count();
    }

    }



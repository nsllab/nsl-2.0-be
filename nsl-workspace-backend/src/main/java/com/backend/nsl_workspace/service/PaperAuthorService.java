package com.backend.nsl_workspace.service;

import com.backend.nsl_workspace.entity.PaperAuthor;
import com.backend.nsl_workspace.repository.PaperAuthorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PaperAuthorService {
    @Autowired
    private PaperAuthorRepository paperAuthorRepository;

    public PaperAuthor createPaperAuthor(PaperAuthor paperAuthor) {
        // Clone the object to ensure we're not reusing the same instance
        PaperAuthor newAuthor = new PaperAuthor();
        newAuthor.setPaperId(paperAuthor.getPaperId());
        newAuthor.setUserId(paperAuthor.getUserId());
        newAuthor.setAuthorNum(paperAuthor.getAuthorNum());

        // Log what we're saving
        System.out.println("Service saving: paperId=" + newAuthor.getPaperId() +
                ", userId=" + newAuthor.getUserId() + ", authorNum=" + newAuthor.getAuthorNum());

        return paperAuthorRepository.save(newAuthor);
    }

    public List<PaperAuthor> getPaperAuthors(Integer paperId) {
        return paperAuthorRepository.findByPaperId(paperId);
    }

    public void deleteByPaperId(Integer paperId) {
        paperAuthorRepository.deleteByPaperId(paperId);
    }
}
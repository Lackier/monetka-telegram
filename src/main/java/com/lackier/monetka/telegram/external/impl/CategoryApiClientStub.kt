package com.lackier.monetka.telegram.external.impl;

import com.lackier.monetka.backend.api.client.CategoryApiClient
import com.lackier.monetka.backend.api.dto.CategoryDto
import org.jeasy.random.EasyRandom
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryApiClientStub : CategoryApiClient {
    private val generator = EasyRandom()
    private val defaultPageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")))

    override fun getCategories(userId: UUID): Page<CategoryDto> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            defaultPageable,
            5
        )
    }

    override fun getCategories(userId: UUID, pageNumber: Int): Page<CategoryDto> {
        return PageImpl(
            listOf(getCategory(), getCategory(), getCategory(), getCategory(), getCategory()),
            getPageable(pageNumber),
            5
        )
    }

    private fun getPageable(pageNumber: Int): Pageable {
        return PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("createdDate")))
    }

    private fun getCategory(): CategoryDto {
        return generator.nextObject(Class.forName("com.lackier.monetka.backend.api.dto.CategoryDto")) as CategoryDto
    }

    override fun createCategory(categoryDto: CategoryDto) {
        println(categoryDto)
        //TODO
    }

    override fun editCategory(categoryDto: CategoryDto) {
        println(categoryDto)
        //TODO
    }

    override fun getCategory(id: UUID): CategoryDto {
        return getCategory()
    }

    override fun deleteCategory(chatId: String, id: UUID) {
        println(chatId + id)
        //TODO
    }
}

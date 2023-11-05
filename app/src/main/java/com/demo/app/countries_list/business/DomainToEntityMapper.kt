package com.demo.app.countries_list.business

import com.demo.app.countries_list.business.model.Country
import com.demo.app.countries_list.business.model.Currency
import com.demo.app.countries_list.business.model.Language
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.countries_list.data.local.entity.CurrencyEntity
import com.demo.app.countries_list.data.local.entity.LanguageEntity

object DomainToEntityMapper {

    fun Country.toEntity(): CountryEntity {
        return CountryEntity(
            capital = this.capital,
            code = this.code,
            currencyId = this.currency.toEntity(),
            flag = this.flag,
            languageId = this.language.toEntity(),
            name = this.name,
            region = this.region
        )
    }

    private fun Currency.toEntity(): Int {
        return CurrencyEntity(
            code = this.code,
            symbol = this.symbol,
            name = this.name
        ).id
    }

    private fun Language.toEntity(): Int {
        return LanguageEntity(
            code = this.code,
            name = this.name
        ).id
    }

    fun List<Country>.toEntityList(): List<CountryEntity> {
        return map { it.toEntity() }
    }
}
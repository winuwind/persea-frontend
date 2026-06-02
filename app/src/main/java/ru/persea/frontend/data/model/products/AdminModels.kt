package ru.persea.frontend.data.model.products

// Factor requests
data class CreateFactorRequest(
    val name: String,
    val typeId: Long,
    val description: String? = null
)

data class CreateUnitRequest(
    val name: String
)

data class CreateFactorTypeRequest(
    val name: String
)

data class CreateNumericRuleRequest(
    val categoryId: Long,
    val unitId: Long,
    val minValue: Double,
    val maxValue: Double
)

data class CreateBooleanRuleRequest(
    val categoryId: Long,
    val impact: Int
)

data class CreateEnumValueRequest(
    val value: String
)

data class CreateEnumRuleRequest(
    val categoryId: Long,
    val valueId: Long,
    val impact: Int
)

// Response models for rules
data class NumericRuleResponse(
    val id: Long,
    val factor: Factor?,
    val category: CategoryDto?,
    val unit: Unit?,
    val minValue: Double,
    val maxValue: Double
)

data class BooleanRuleResponse(
    val id: Long,
    val factor: Factor?,
    val category: CategoryDto?,
    val impact: Int
)

data class EnumValueResponse(
    val id: Long,
    val factor: Factor?,
    val value: String
)

data class EnumRuleResponse(
    val id: Long,
    val category: CategoryDto?,
    val enumValue: EnumValueResponse?,
    val impact: Int
)
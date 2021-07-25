package com.godaddy.namesearch.repository.storage

import com.google.gson.annotations.SerializedName

data class Auth(val token: String)

object AuthManagerNew {
    var user: User? = null
    var token = String()
}

data class DomainSearchProductResponse(
    @SerializedName("PriceInfo") val priceInfo: PriceInfo,
    @SerializedName("Content") val content: Content?,
    @SerializedName("ProductId") val productId: Int
)

data class PriceInfo(
    @SerializedName("CurrentPriceDisplay") val currentPriceDisplay: String,
    @SerializedName("ListPriceDisplay") val listPriceDisplay: String,
    @SerializedName("PromoRegLengthFlag") val promoLength: Int,
)

data class Content (
    val header: String?,
    val messages: String?,
    val phases: List<String>?,
    val subHeader: String?,
    @SerializedName("TLD") val tld: String
)
data class DomainSearchRecommendedResponse(
    @SerializedName("Products") val products: List<DomainSearchProductResponse>,
    @SerializedName("RecommendedDomains") val domains: List<RecommendedDomain>
)

data class RecommendedDomain(
    @SerializedName("Fqdn") val fqdn: String,
    @SerializedName("Extension") val tld: String,
    val tierId: Int,
    @SerializedName("IsPremiumTier") val isPremium: Boolean,
    @SerializedName("ProductId") val productId: Int,
    val inventory: String
)
data class DomainSearchExactMatchResponse (
    @SerializedName("Products") val products: List<DomainSearchProductResponse>,
    @SerializedName("ExactMatchDomain") val domain: ExactMatch
)

data class ExactMatch (
    @SerializedName("Fqdn") val fqdn: String,
    @SerializedName("Extension") val tld: String,
    val tierId: Int,
    val isAvailable: Boolean,
    @SerializedName("IsPremiumTier") val isPremium: Boolean,
    @SerializedName("ProductId") val productId: Int
)

data class LoginResponse(
    val auth: Auth,
    val user: User
)

data class LoginRequest(
    val user: String,
    val pwd: String,
)

object PaymentsManagerNew {
    var selectedPaymentMethod: PaymentMethod = PaymentMethod(token = "token", name = "name", lastFour = "last", displayFormattedEmail = "email")
}

data class PaymentMethod(
    val name: String,
    val token: String,
    val lastFour: String?,
    val displayFormattedEmail: String?
)

data class PaymentRequest(
    val auth: String,
    val token: String,
)

object ShoppingCartNew {
    var domains: MutableList<Domain> = mutableListOf()
}

data class User(
    val first: String,
    val last: String
)

data class Domain(
    val name: String,
    val price: String,
    val productId: Int,
    var selected: Boolean = false
)

data class NewsSource(
    val id: String,
    val name: String
)

data class NewsArticleItem(
    val source: NewsSource,
    val author: String,
    val title: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class NewsFeedItem(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticleItem>
)
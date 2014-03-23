#include "entity.hpp"

namespace RSC {
	namespace Model {
		Entity::Entity(unsigned short index, unsigned short x, unsigned short y) {
				this->index = index;
				this->x = x;
				this->y = y;
		}

		unsigned short Entity::getIndex() {
				return this->index;
		}

		void Entity::setIndex(unsigned short index) {
				this->index = index;
		}

		unsigned short Entity::getX() {
				return this->x;
		}

		void Entity::setX(unsigned short x) {
				this->x = x;
		}

		unsigned short Entity::getY() {
				return this->y;
		}

		void Entity::setY(unsigned short y) {
				this->y = y;
		}
	}
}

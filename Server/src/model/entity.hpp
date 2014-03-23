#ifndef _ENTITY_HPP
#define _ENTITY_HPP

namespace RSC {
	namespace Model {
		class Entity {
		protected:
				unsigned short index;
				unsigned short x;
				unsigned short y;

		public:
				Entity(unsigned short, unsigned short, unsigned short);
				unsigned short getIndex();
				void setIndex(unsigned short);
				unsigned short getX();
				void setX(unsigned short);
				unsigned short getY();
				void setY(unsigned short);
		};
	}
}

#endif
